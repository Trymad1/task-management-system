package com.trymad.task_management.web.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
@Constraint(validatedBy = PriorityValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskPriorityCheck {

    String message() default "Invalid priority";

    Class<?>[] groups() default {}; 

    boolean allowNull() default false;

    Class<? extends Payload>[] payload() default {};
}
