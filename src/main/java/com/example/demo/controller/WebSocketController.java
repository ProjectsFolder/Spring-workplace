package com.example.demo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Secured("ROLE_USER")
@RequestMapping(path="/socket", name="socket")
public class WebSocketController {
    @GetMapping(value = "/", name = "index")
    public String index(Model model) {

        return "socket/index";
    }
}
