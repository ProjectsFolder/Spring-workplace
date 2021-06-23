package com.example.demo.validator.repeatable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class RepeatableValidator implements ConstraintValidator<Repeatable, Object> {
    String[] fields;

    @Override
    public void initialize(Repeatable constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        String[] getters = Arrays.stream(fields)
                .map(e -> "get" + e.substring(0, 1).toUpperCase(Locale.ROOT) + e.substring(1))
                .toArray(String[]::new);

        HashMap<String, Method> entityGetters = new HashMap<>();
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            entityGetters.put(method.getName(), method);
        }

        String value = "";
        for (String getter : getters) {
            if (entityGetters.containsKey(getter)) {
                try {
                    String newValue = entityGetters.get(getter).invoke(obj).toString();
                    if (value.isEmpty()) {
                        value = newValue;
                    } else if (!newValue.equals(value)) {
                        return false;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("В момент вызова метода \"" + getter + "\" произошла ошибка.");
                }
            } else {
                throw new RuntimeException("Не найден метод \"" + getter + "\" в сущности.");
            }
        }

        return true;
    }
}
