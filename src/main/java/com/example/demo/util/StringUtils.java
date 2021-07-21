package com.example.demo.util;

import java.util.Optional;

public class StringUtils {
    public static Optional<String> getExtensionByFilename(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                ;
    }
}
