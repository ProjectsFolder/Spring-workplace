package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = "/api", name = "api")
public class ApiController {

    //@Secured("ROLE_ADMIN")
    @GetMapping(path = "/admin", name = "admin")
    public String getAdmin() {
        return "Hi admin";
    }

    //@Secured("ROLE_USER")
    @GetMapping(path = "/user", name = "user")
    public String getUser() {
        return "Hi user";
    }
}
