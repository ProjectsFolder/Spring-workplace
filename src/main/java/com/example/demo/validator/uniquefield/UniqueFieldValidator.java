package com.example.demo.validator.uniquefield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;

public class UniqueFieldValidator implements ConstraintValidator<UniqueField, Object> {
    @Autowired
    private ApplicationContext applicationContext;

    Class<?> entity;
    String filed;

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

        Repositories repositories = new Repositories(this.applicationContext);
        Optional<?> optional = repositories.getRepositoryFor(this.entity);
        if (!optional.isPresent()) {
            throw new RuntimeException("Не найден репозиторий для сущности \"" + this.entity + "\"");
        }

        Method target = null;
        String field = this.filed.substring(0, 1).toUpperCase(Locale.ROOT) + this.filed.substring(1);
        String targetMethodName = "findBy" + field;
        JpaRepository<?, ?> repository = (JpaRepository<?, ?>) optional.get();
        Method[] methods = repository.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            Class<?>[] parameters = method.getParameterTypes();
            if (targetMethodName.equals(methodName) && 1 == parameters.length) {
                Class<?> parameter = parameters[0];
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
