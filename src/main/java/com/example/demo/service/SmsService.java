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
public class SmsService extends WebRequest {

    public static final String AUTH_CHANNEL = "auth";

    private final String key;
    private final String url;

    public SmsService(@Autowired Environment environment)
    {
        this.key = environment.getProperty("sms.key");
        this.url = environment.getProperty("sms.url");
    }

    public ArrayList<Map<String, Object>> send(String phone, String message, String channel) {
        var params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("message", message);
        params.put("channel", channel);

        return this.sendRequest("send_message", params);
    }

    @SuppressWarnings("SameParameterValue")
    private ArrayList<Map<String, Object>> sendRequest(String action, Map<String, String> params) {
        params.put("api_key", this.key);

        var builder = UriComponentsBuilder.fromHttpUrl(this.url + "/" + action);
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
