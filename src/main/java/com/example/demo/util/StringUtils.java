package com.example.demo.util;

import java.util.Optional;

public class StringUtils {
    public static Optional<String> getExtensionByFilename(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                ;
    }

    public static String reverseString(String string) {
        return new StringBuilder(string).reverse().toString();
    }

    public static Boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }

        return true;
    }

    public static Boolean isBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }
}
