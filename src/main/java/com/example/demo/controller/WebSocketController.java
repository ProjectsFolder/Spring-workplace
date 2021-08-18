package com.example.demo.controller;

import com.example.demo.dto.MessageDto;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/socket", name="socket")
public class WebSocketController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Secured("ROLE_USER")
    @GetMapping(value = "/", name = "index")
    public String index(
        Model model,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        model.addAttribute("currentUser", userRepository.findByUsername(currentUser.getUsername()));
        model.addAttribute("users", userRepository.findAll());

        return "socket/index";
    }

    @MessageMapping("/test")
    @SendTo("/task/messages")
    public MessageDto send(MessageDto messageDto) {
        return messageDto;
    }

    @MessageMapping("/secured/test")
    public void securedSend(MessageDto messageDto) {
        simpMessagingTemplate.convertAndSendToUser(
            messageDto.getRecipientId(),
            "/messages",
            messageDto
        );
    }
}
