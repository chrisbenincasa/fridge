package com.chrisbenincasa.food.controllers

import com.chrisbenincasa.food.config.FoodConfig
import com.chrisbenincasa.food.db.DbAccess
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import javax.inject.Inject
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext

class AdminController @Inject()(
  config: FoodConfig,
  db: DbAccess[_]
)(implicit executionContext: ExecutionContext) extends Controller {
  prefix("/admin/v1") {

    get("/config", admin = true) { _: Request =>
      config
    }

    get("/meta", admin = true) { _: Request =>
      db.run(MTable.getTables).map(_.toList)
    }
  }
}
