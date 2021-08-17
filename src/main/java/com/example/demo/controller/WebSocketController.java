package com.example.demo.controller;

import com.example.demo.dto.MessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/socket", name="socket")
public class WebSocketController {

    @Secured("ROLE_USER")
    @GetMapping(value = "/", name = "index")
    public String index() {
        return "socket/index";
    }

    @MessageMapping("/test")
    @SendTo("/broker/messages")
    public MessageDto send(MessageDto messageDto) {
        return messageDto;
    }
}
