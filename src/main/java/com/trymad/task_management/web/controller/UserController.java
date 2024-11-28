package com.trymad.task_management.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User controller")

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Get all users")
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<UserDTO> getAll() {
        return userMapper.toDto(userService.getAll());
    }

    @Parameters({
        @Parameter(name = "userId", example = "1")
    })
    @Operation(summary = "Get user by userId")
    @GetMapping("{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserDTO getByUserId(@PathVariable Long userId) {
        return userMapper.toDto(userService.get(userId));
    }

    @Parameters({
        @Parameter(name = "userId", example = "1")
    })
    @Operation(summary = "Update user", 
    description = "user can update only own profile")
    @PutMapping("{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserDTO update(@PathVariable Long userId,
            @RequestBody @Valid UserUpdateDTO userUpdateDTO) throws AccessDeniedException {
        return userMapper.toDto(userService.update(userUpdateDTO, userId));
    }

    // @PreAuthorize("hasAuthority('ADMIN')") // Disabled for eaiser api test
    @Parameters({
        @Parameter(name = "userId", example = "1")
    })
    @Operation(summary = "Grant admin role to user", description = "created only for easier api test, don`t required admin role, only authentication and useruserId")
    @PostMapping("{userId}/grantAdmin")
    @ResponseStatus(value = HttpStatus.OK)
    public UserDTO grantAdminRole(@PathVariable Long userId) {
        return userMapper.toDto(userService.grantAdminRole(userId));
    }
}
