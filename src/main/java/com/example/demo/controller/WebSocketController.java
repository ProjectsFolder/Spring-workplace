package com.example.demo.controller;

import com.example.demo.dto.MessageDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Secured("ROLE_USER")
@RequestMapping(path="/socket", name="socket")
public class WebSocketController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping(value = "/", name = "index")
    public String index(
        Model model,
        Authentication authentication,
        @Value("${app.api.token}") String token
    ) {
        var userId = 0L;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            userId = userRepository.findByUsername(authentication.getName()).getId();
        }

        model.addAttribute("currentUserId", userId);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("token", token);

        return "socket/index";
    }

    @Secured("ROLE_ADMIN")
    @MessageMapping("/test")
    @SendTo("/task/messages")
    public MessageDto send(MessageDto messageDto) {
        return messageDto;
    }

    @MessageMapping("/user/test")
    public void sendToUser(MessageDto messageDto, Authentication authentication) {
        var user = authentication.getPrincipal();
        if (user instanceof User) {
            messageDto.setSenderId(((User) user).getId().toString());
        }
        if (!messageDto.getText().isEmpty()) {
            simpMessagingTemplate.convertAndSendToUser(
                messageDto.getRecipientId(),
                "/messages",
                messageDto
            );
        }
    }
}
