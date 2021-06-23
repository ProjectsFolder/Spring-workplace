package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
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
        HashMap<String, String> params = new HashMap<>();
        params.put("module", "v2.contract");
        params.put("action", "GetDetailedById");
        params.put("id", String.valueOf(id));

        return this.sendRequest(params);
    }

    private ArrayList<Map<String, Object>> sendRequest(Map<String, String> params) {
        params.put("user", this.username);
        params.put("pswd", this.password);
        params.put("ct", "json");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class);

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> result = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            int success = Integer.parseInt(result.get("success").toString());
            if (0 == success) {
                String error = (String) result.get("message");
                throw new RuntimeException("Запрос к системе завершен с ошибкой: " + error);
            }

            Object data = result.get("data");
            if (data instanceof ArrayList) {
                //noinspection unchecked
                return (ArrayList<Map<String, Object>>) data;
            } else if (data instanceof Map) {
                ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>() {};
                //noinspection unchecked
                list.add((Map<String, Object>) data);

                return list;
            } else if (data != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("result", data);
                ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>() {};
                list.add(map);

                return list;
            }

            throw new RuntimeException("Ответ от системы невозможно обработать.");
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Ответ от системы невозможно обработать.");
        }
    }
}
