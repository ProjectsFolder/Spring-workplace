package com.example.demo.validator.uniquefield;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueFieldValidator.class)
public @interface UniqueField {
    String message() default "Поле не уникально";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Class<?> entity();
    String field();
}
