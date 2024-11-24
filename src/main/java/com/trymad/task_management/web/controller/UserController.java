package com.trymad.task_management.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trymad.task_management.service.UserService;
import com.trymad.task_management.web.dto.user.UserCreateDTO;
import com.trymad.task_management.web.dto.user.UserDTO;
import com.trymad.task_management.web.dto.user.UserMapper;
import com.trymad.task_management.web.dto.user.UserUpdateDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("{id}")
    public UserDTO getById(@PathVariable Long id) {
        return null;
    }

    @PostMapping
    public UserDTO create(@RequestBody UserCreateDTO userCreateDTO) {
        return null;
    }

    @PutMapping("{id}")
    public UserDTO update(@PathVariable Long id, 
                          @RequestBody UserUpdateDTO userUpdateDTO) {
        return null;
    }
}
