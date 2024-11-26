package com.trymad.task_management.web.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.trymad.task_management.service.UserService;
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

    @GetMapping
    public List<UserDTO> getAll() {
        return userMapper.toDto(userService.getAll());
    }

    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserDTO getById(@PathVariable Long id) {
        return userMapper.toDto(userService.get(id));
    }

    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserDTO update(@PathVariable Long id,
            @RequestBody UserUpdateDTO userUpdateDTO) throws AccessDeniedException {
        return userMapper.toDto(userService.update(userUpdateDTO, id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{id}/grantAdmin")
    public UserDTO grantAdminRole(@PathVariable Long id) {
        return userMapper.toDto(userService.grantAdminRole(id));
    }
}
