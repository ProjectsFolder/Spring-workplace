package com.example.demo.service.base;

import com.example.demo.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class WebRequest {

    protected ArrayList<Map<String, Object>> handleResponse(String body) {
        var mapper = new ObjectMapper();
        Map<String, Object> result;
        try {
            result = mapper.readValue(body, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Ответ от системы невозможно обработать.");
        }

        var success = 0;
        var temp = result.get("success").toString();
        if (StringUtils.isBoolean(temp)) {
            success = Boolean.parseBoolean(temp) ? 1 : 0;
        } else if (StringUtils.isInteger(temp)) {
            success = Integer.parseInt(temp);
        }
        if (0 == success) {
            var error = (String) Optional.ofNullable(result.get("message")).orElse("Неизвестная ошибка");
            throw new RuntimeException("Запрос к системе завершен с ошибкой: " + error);
        }

        var data = result.get("data");
        if (data instanceof ArrayList) {
            //noinspection unchecked
            return (ArrayList<Map<String, Object>>) data;
        } else if (data instanceof Map) {
            var list = new ArrayList<Map<String, Object>>() {};
            //noinspection unchecked
            list.add((Map<String, Object>) data);

            return list;
        } else if (data != null) {
            var map = new HashMap<String, Object>();
            map.put("result", data);
            var list = new ArrayList<Map<String, Object>>() {};
            list.add(map);

            return list;
        }

        throw new RuntimeException("Ответ от системы невозможно обработать.");
    }
}
