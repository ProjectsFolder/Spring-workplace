package com.example.demo.controller;

import com.example.demo.classes.ApiSuccessResponse;
import com.example.demo.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = "/grpc", name = "grpc")
public class GrpcController {

    @Autowired
    private GrpcService grpcService;

    @GetMapping(path = "/send", name = "send")
    public ApiSuccessResponse send(
            @RequestParam(defaultValue = "asylum29") String name
    ) {
        var message = this.grpcService.send(name);

        return new ApiSuccessResponse(Map.of(
                "message", message
        ));
    }
}
