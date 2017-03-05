package com.chrisbenincasa.food.db.model

import slick.jdbc.JdbcProfile

case class FoodItem(
  id: Option[Long],
  name: String,
  quantity: Double,
  unit: FoodItemUnit
)

class FoodItems(val driver: JdbcProfile) {
  import driver.api._

  type TableType = FoodItemsTable

  implicit val foodItemUnitMapper = MappedColumnType.base[FoodItemUnit, String](
    e => e.toString.toLowerCase(),
    s => FoodItemUnit.values.find(_.toString.toLowerCase == s).get
  )

  class FoodItemsTable(tag: Tag) extends Table[FoodItem](tag, "food_items") {
    def id = column[Long]("id", O.AutoInc)
    def name = column[String]("name")
    def quantity = column[Double]("quantity")
    def unit = column[FoodItemUnit]("unit")
    def createdTime = column[Long]("created_time")
    def updatedTime = column[Long]("updated_time")
    def isDeeleted = column[Boolean]("is_deleted", O.Default(false))
    def * = (id.?, name, quantity, unit) <> (FoodItem.tupled, FoodItem.unapply)
  }

  val query = TableQuery[FoodItemsTable]

  //
  // Accessors
  //

  def getFoodItemsQuery(inStockOnly: Boolean): StreamingDBIO[Seq[FoodItem], FoodItem] = {
    val filterFunc = if (inStockOnly) (t: TableType) => t.quantity > 0.0 else (_: TableType) => true.bind
    query.filter(filterFunc).result
  }
}
