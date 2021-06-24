package com.example.demo.validator.uniquefield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.Locale;

public class UniqueFieldValidator implements ConstraintValidator<UniqueField, Object> {
    @Autowired
    private ApplicationContext applicationContext;

    private Class<?> entity;

    private String filed;

    @Override
    public void initialize(UniqueField constraintAnnotation) {
        org.springframework.web.context.support.SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        this.entity = constraintAnnotation.entity();
        this.filed = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (null == value) {
            return false;
        }

        var repositories = new Repositories(this.applicationContext);
        var optional = repositories.getRepositoryFor(this.entity);
        if (optional.isEmpty()) {
            throw new RuntimeException("Не найден репозиторий для сущности \"" + this.entity + "\"");
        }

        Method target = null;
        var field = this.filed.substring(0, 1).toUpperCase(Locale.ROOT) + this.filed.substring(1);
        var targetMethodName = "findBy" + field;
        var repository = (JpaRepository<?, ?>) optional.get();
        var methods = repository.getClass().getMethods();
        for (var method : methods) {
            var methodName = method.getName();
            var parameters = method.getParameterTypes();
            if (targetMethodName.equals(methodName) && 1 == parameters.length) {
                var parameter = parameters[0];
                if (parameter == value.getClass()) {
                    target = method;
                    break;
                }
            }
        }

        if (null == target) {
            throw new RuntimeException("Не найден метод поиска \"" + targetMethodName + "\" в репозитории.");
        }

        try {
            return target.invoke(repository, value) == null;
        } catch (Exception e) {
            throw new RuntimeException("В момент вызова метода произошла ошибка");
        }
    }
}
