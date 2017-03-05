//
// Copyright (c) 2011-2017 by Curalate, Inc.
//

package com.chrisbenincasa

import com.chrisbenincasa.food.validators.MinStrictInternal
import scala.annotation.meta.param

package object food {
  type MinStrict = MinStrictInternal@param
}
