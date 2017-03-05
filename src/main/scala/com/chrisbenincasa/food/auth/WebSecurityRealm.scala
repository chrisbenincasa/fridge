package com.chrisbenincasa.food.auth

import com.chrisbenincasa.food.db.DbAccess
import javax.inject.Inject
import org.apache.shiro.authc._
import org.apache.shiro.authz.{AuthorizationException, AuthorizationInfo, SimpleAuthorizationInfo}
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.util.SimpleByteSource
import scala.collection.JavaConversions._
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration

object WebSecurityRealm {
  private val REALM_NAME = "webRealm"
}

class WebSecurityRealm @Inject()(db: DbAccess[_])(implicit executionContext: ExecutionContext) extends AuthorizingRealm {
  import WebSecurityRealm._


  override def getAuthenticationTokenClass: Class[WebAuthenticationToken] = classOf[WebAuthenticationToken]

  override def doGetAuthenticationInfo(token: AuthenticationToken): AuthenticationInfo = {
    val email = token.asInstanceOf[WebAuthenticationToken].getPrincipal().email

    val fut = db.run {
      db.users.findByEmail(email)
    }

    Await.result(fut, Duration.Inf) match {
      case Some((user, creds)) =>
        val fullPrincipal = WebPrincipal(user.id.get, user.name, user.email)
        new SimpleAuthenticationInfo(fullPrincipal, creds.password, new SimpleByteSource(creds.salt), REALM_NAME)
      case None => throw new UnknownAccountException
    }
  }

  override def doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo = {
    if (principals == null) {
      throw new AuthorizationException("PrincipalCollection method argument cannot be null.")
    }

    val principal = getAvailablePrincipal(principals).asInstanceOf[WebPrincipal]

    Await.result(db.run(db.users.findById(principal.userId)), Duration.Inf) match {
      case Some(_) => new SimpleAuthorizationInfo(Set.empty[String])
      case None => throw new RuntimeException("")
    }
  }

  override protected def isPermitted(principals: PrincipalCollection, path: String): Boolean = true
}

case class WebPrincipal(
  userId: Long,
  name: String,
  email: String
)

case class WebUnauthenticatedPrincipal(email: String)

class WebAuthenticationToken(
  val email: String,
  val password: String,
  val rememberMe: Boolean,
  val ip: String,
  val userAgent: Option[String]
) extends RememberMeAuthenticationToken {
  def getCredentials(): String = password

  def getPrincipal(): WebUnauthenticatedPrincipal = WebUnauthenticatedPrincipal(email)

  def isRememberMe(): Boolean = rememberMe
}
