package com.example.demo.validator.repeatable;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RepeatableValidator.class)
public @interface Repeatable {
    String message() default "Поля не совпадают";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] fields();
}
