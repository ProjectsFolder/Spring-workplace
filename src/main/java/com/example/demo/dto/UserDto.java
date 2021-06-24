package com.example.demo.dto;

import com.example.demo.entity.User;
import com.example.demo.validator.group.OnCreate;
import com.example.demo.validator.group.OnUpdate;
import com.example.demo.validator.repeatable.Repeatable;
import com.example.demo.validator.uniquefield.UniqueField;

import javax.validation.constraints.Size;

@Repeatable(
        fields = {"password", "passwordConfirm"},
        message = "Пароли не совпадают",
        groups = {OnCreate.class, OnUpdate.class}
)
public class UserDto {
    @UniqueField(entity = User.class, field = "username", message = "Логин уже используется", groups = OnCreate.class)
    @Size(min = 5, message = "Не меньше 5 знаков", groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @Size(min = 5, message = "Не меньше 5 знаков", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    private String passwordConfirm;

    public UserDto() { }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
