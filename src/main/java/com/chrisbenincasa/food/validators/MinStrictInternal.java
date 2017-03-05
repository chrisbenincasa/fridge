package com.chrisbenincasa.food.validators;

import com.chrisbenincasa.food.validators.MinStrictValidator;
import com.twitter.finatra.validation.Validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ PARAMETER })
@Retention(RUNTIME)
@Validation(validatedBy = MinStrictValidator.class)
public @interface MinStrictInternal {
    long value();
}
