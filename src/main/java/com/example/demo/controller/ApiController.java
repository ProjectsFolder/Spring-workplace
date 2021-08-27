package com.example.demo.controller;

import com.example.demo.classes.ApiErrorResponse;
import com.example.demo.classes.ApiSuccessResponse;
import com.example.demo.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = "/api", name = "api")
public class ApiController {

    @Autowired
    private SmsService smsService;

    //@Secured("ROLE_ADMIN")
    @GetMapping(path = "/admin", name = "admin")
    public ApiSuccessResponse getAdmin() {
        return new ApiSuccessResponse(Map.of(
            "message", "Hi, admin"
        ));
    }

    //@Secured("ROLE_USER")
    @GetMapping(path = "/user", name = "user")
    public ApiSuccessResponse getUser() {
        return new ApiSuccessResponse(Map.of(
            "message", "Hi, user"
        ));
    }

    @GetMapping(path = "/sms", name = "sms")
    public ApiSuccessResponse sendSms() {
        smsService.send("71111111111", "Тест test", SmsService.AUTH_CHANNEL);

        return new ApiSuccessResponse();
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleException(Throwable exception) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(exception.getMessage()));
    }
}
