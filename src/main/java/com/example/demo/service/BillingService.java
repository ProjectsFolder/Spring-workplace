package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class BillingService {

    private final String username;
    private final String password;
    private final String url;

    public BillingService(@Autowired Environment environment)
    {
        this.username = environment.getProperty("billing.username");
        this.password = environment.getProperty("billing.password");
        this.url = environment.getProperty("billing.url");
    }

    public ArrayList<Map<String, Object>> getContract(int id) {
        var params = new HashMap<String, String>();
        params.put("module", "v2.contract");
        params.put("action", "GetDetailedById");
        params.put("id", String.valueOf(id));

        return this.sendRequest(params);
    }

    private ArrayList<Map<String, Object>> sendRequest(Map<String, String> params) {
        params.put("user", this.username);
        params.put("pswd", this.password);
        params.put("ct", "json");

        var builder = UriComponentsBuilder.fromHttpUrl(this.url);
        for (var entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        var restTemplate = new RestTemplate();
        var response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class);

        var mapper = new ObjectMapper();

        Map<String, Object> result;
        try {
            result = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Ответ от системы невозможно обработать.");
        }

        var success = Integer.parseInt(result.get("success").toString());
        if (0 == success) {
            var error = (String) result.get("message");
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
