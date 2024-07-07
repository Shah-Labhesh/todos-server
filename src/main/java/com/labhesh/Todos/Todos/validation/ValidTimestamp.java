package com.labhesh.Todos.Todos.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TimestampValidatorImpl.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimestamp {
    String message() default "Invalid timestamp";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean isFuture() default false;
}
