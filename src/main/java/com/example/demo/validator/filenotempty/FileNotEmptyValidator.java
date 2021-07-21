package com.example.demo.validator.filenotempty;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileNotEmptyValidator implements ConstraintValidator<FileNotEmpty, Object> {
    @Override
    public void initialize(FileNotEmpty constraintAnnotation) { }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (null == value) {
            return false;
        }

        if (!(value instanceof MultipartFile)) {
            throw new RuntimeException("Для валидации поддерживается только тип MultipartFile, получен: " + value.getClass());
        }

        var file = (MultipartFile) value;

        return !file.isEmpty();
    }
}
