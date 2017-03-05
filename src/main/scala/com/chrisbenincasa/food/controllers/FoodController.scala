package com.chrisbenincasa.food.controllers

import com.chrisbenincasa.food.MinStrict
import com.chrisbenincasa.food.config.FoodConfig
import com.chrisbenincasa.food.db.DbAccess
import com.chrisbenincasa.food.db.model.{FoodItem, FoodItemUnit}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.request.QueryParam
import com.twitter.finatra.validation.NotEmpty
import javax.inject.Inject
import scala.concurrent.ExecutionContext

case class FoodResponse(id: Long)

case class GetFoodRequast(
  @QueryParam
  inStock: Boolean = false
)

case class CreateFoodRequest(
  @NotEmpty  name: String,
  @MinStrict(value = 0) quantity: Double,
  unit: FoodItemUnit
) {
  def toDAO = FoodItem(
    id = None,
    name = name,
    quantity = quantity,
    unit = unit
  )
}

class FoodController @Inject()(
  config: FoodConfig,
  db: DbAccess[_]
)(implicit executionContext: ExecutionContext) extends Controller {
  import db.api._

  prefix("/v1/food") {

    get("/") { req: GetFoodRequast =>
      db.run(db.foodItems.getFoodItemsQuery(req.inStock))
    }

    get("/:id") { req: Request =>
      val id = req.getLongParam("id")
      db.run(db.foodItems.query.filter(_.id === id).result.headOption).map {
        case Some(item) => response.ok(item)
        case None => response.notFound
      }
    }

    post("/") { req: CreateFoodRequest =>
      db.run {
        (db.foodItems.query returning db.foodItems.query.map(_.id) into((dao, id) =>
          dao.copy(id = Some(id))
        )) += req.toDAO
      }.map(response.created(_))
    }
  }
}
