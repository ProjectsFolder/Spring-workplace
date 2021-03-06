package com.example.demo.service;

import com.example.demo.exception.IgnoreErrorHandler;
import com.example.demo.service.base.WebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class BillingService extends WebRequest {

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
        var url = URLDecoder.decode(builder.toUriString(), StandardCharsets.UTF_8);

        var restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new IgnoreErrorHandler());
        var response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        return this.handleResponse(response.getBody());
    }
}
