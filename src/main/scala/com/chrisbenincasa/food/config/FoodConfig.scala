package com.chrisbenincasa.food.config

import com.chrisbenincasa.food.util.ClassNameJsonSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.sql.Driver

case class DbConfig(
  @JsonSerialize(using = classOf[ClassNameJsonSerializer])
  driver: Driver,
  url: String,
  user: String,
  password: String
)

case class FoodConfig(
  cwd: String,
  mode: String = "multi user",
  db: DbConfig
)

