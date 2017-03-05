package com.chrisbenincasa.food.config

import com.typesafe.config.Config
import java.sql.Driver
import net.ceedubs.ficus.readers.{AllValueReaderInstances, ValueReader}

object CustomReaders {
  implicit val classReader = new ValueReader[Driver] {
    override def read(config: Config, path: String): Driver = {
      Class.forName(config.getString(path)).newInstance().asInstanceOf[Driver]
    }
  }
}
