package com.example.demo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Secured("ROLE_USER")
@RequestMapping(path="/keycloak", name="keycloak")
public class KeycloakController {
    @GetMapping(path = "/", name = "index")
    public String index(Model model, Authentication authentication) {
        model.addAttribute("currentUser", authentication.getName());

        return "keycloak/index";
    }
}
