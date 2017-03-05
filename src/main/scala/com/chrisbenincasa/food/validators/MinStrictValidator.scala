//
// Copyright (c) 2011-2017 by Curalate, Inc.
//

package com.chrisbenincasa.food.validators

import com.chrisbenincasa.food.MinStrict
import com.twitter.finatra.validation._

object MinStrictValidator {
  def errorMessage(
    resolver: ValidationMessageResolver,
    value: Any,
    minValue: Long
  ): String = {
    resolver.resolve(classOf[Min], value, minValue)
  }
}

class MinStrictValidator(
  validationMessageResolver: ValidationMessageResolver,
  annotation: MinStrict
) extends Validator[MinStrict, Any](
  validationMessageResolver,
  annotation
) {
  private val minValue = annotation.value()

  override def isValid(value: Any): ValidationResult = {
    value match {
      case numberValue: Number =>
        ValidationResult.validate(
          minValue < numberValue.doubleValue(),
          errorMessage(numberValue),
          errorCode(numberValue)
        )
      case _ =>
        throw new IllegalArgumentException(s"Class [${value.getClass}] is not supported")
    }
  }

  private def errorMessage(value: Number) = {
    MinStrictValidator.errorMessage(validationMessageResolver, value, minValue)
  }

  private def errorCode(value: Number) = {
    ErrorCode.ValueTooSmall(minValue, value)
  }
}
