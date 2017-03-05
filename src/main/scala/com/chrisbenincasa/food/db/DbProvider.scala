package com.chrisbenincasa.food.db

import com.chrisbenincasa.food.DbExecutionContext
import com.chrisbenincasa.food.db.model.{FoodItem, FoodItems, UserCredentials, Users}
import java.util.concurrent.RejectedExecutionException
import javax.sql.DataSource
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

class DbProvider[T <: JdbcProfile](val dataSource: DataSource, val driver: T) {
  import driver.api._
  val db = Database.forDataSource(dataSource, None)
}

class DbAccess[T <: JdbcProfile](
  val provider: DbProvider[T]
)(@DbExecutionContext implicit val executionContext: ExecutionContext) {
  val api = provider.driver.api

  import api._

  val foodItems = new FoodItems(provider.driver)
  val users = new Users(provider.driver)
  val userCredentials = new UserCredentials(provider.driver)

  //
  // Users
  //

  //
  // Food Items
  //

  def getFeedItems(inStockOnly: Boolean = false): Future[Seq[FoodItem]] = {
    run {
      foodItems.getFoodItemsQuery(inStockOnly)
    }
  }

  def run[R, S <: NoStream, E <: Effect](action: DBIOAction[R, S, E]): Future[R] = {
    provider.db.run(action).recoverWith {
      case _: RejectedExecutionException => Future.failed(SlickDBNoAvailableThreadsException("DB thread pool is busy and queue is full, try again"))
    }
  }

//  def stream[R, S <: Streaming[_]](action: StreamingDBIO[R, S])(implicit executionContext: ExecutionContext) = {
//    provider.db.stream(action)
//  }
}

case class SlickDBNoAvailableThreadsException(message: String) extends Exception(message)

