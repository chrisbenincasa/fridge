package com.chrisbenincasa.food.controllers

import com.chrisbenincasa.food.auth.WebAuthenticationToken
import com.chrisbenincasa.food.config.FoodConfig
import com.chrisbenincasa.food.db.DbAccess
import com.chrisbenincasa.food.filters.AuthenticatedFilter
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import javax.inject.Inject
import org.apache.shiro.SecurityUtils
import scala.concurrent.ExecutionContext

case class CreateUserRequest(
  email: String,
  name: String,
  password: String
)

case class LoginRequest(
  email: String,
  password: String,
  redirect_url: Option[String]
)

class AuthController @Inject()(
  config: FoodConfig,
  db: DbAccess[_]
)(implicit executionContext: ExecutionContext) extends Controller  {
  // TODO: HTTPS
  prefix("/v1/users") {
    // Log in user based on creds
    post("/login") { req: LoginRequest =>
      val token = new WebAuthenticationToken(req.email, req.password, rememberMe = false, "", None)
      val success = try {
        SecurityUtils.getSubject.login(token)
        SecurityUtils.getSubject.getSession()
        true
      } catch {
        case e: Exception => {
          logger.error(e)
          false
        }
      }

      if (success) {
        req.redirect_url.map(ru => {
          response.temporaryRedirect.header("Location", ru)
        }).getOrElse {
          response.ok()
        }
      } else {
        response.unauthorized
      }
    }

    // Log current user out
    post("/logout") { _: Request =>
      SecurityUtils.getSubject.logout()
      response.ok()
    }

    // Create a user
    post("/") { req: CreateUserRequest =>
      val resultingId = db.run(db.users.newUser(req.name, req.email, req.password))

      resultingId
    }
  }

  // Bug in Finatra, can't put these in the prefix block
  // https://github.com/twitter/finatra/issues/392

  filter[AuthenticatedFilter].get("/v1/users/protected") { _: Request =>
    response.ok("YAY")
  }

  filter[AuthenticatedFilter].get("/v1/users/me") { _: Request =>
    response.ok(SecurityUtils.getSubject.getPrincipal)
  }
}
