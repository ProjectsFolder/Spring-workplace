package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.validator.group.OnCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/user", name="user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/registration", name = "registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDto());

        return "user/registration";
    }

    @PostMapping(value = "/registration", name = "create")
    public String addUser(
            @ModelAttribute("userForm") @Validated(OnCreate.class) UserDto userForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "user/registration";
        }

        userService.saveUser(userForm);

        return "redirect:/";
    }
}
