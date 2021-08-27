package com.example.demo.util;

import org.springframework.stereotype.Component;

@Component
public class TestUtils {
    public String reverseString(String string) {
        return StringUtils.reverseString(string);
    }
}
