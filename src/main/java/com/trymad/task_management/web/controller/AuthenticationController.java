package com.trymad.task_management.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trymad.task_management.service.AuthenticationService;
import com.trymad.task_management.web.dto.jwt.JwtResponse;
import com.trymad.task_management.web.dto.user.LoginDTO;
import com.trymad.task_management.web.dto.user.UserCreateDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Tag(name = "Authentication controller")

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("login")
    public JwtResponse createTokenByCredentionals(@RequestBody @Valid LoginDTO loginDTO) {
        return new JwtResponse(authService.createJwtToken(loginDTO));
    }

    @PostMapping("registry")
    public JwtResponse registy(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        return new JwtResponse(authService.registryUser(userCreateDTO));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("refresh")
    public JwtResponse refreshToken(@RequestHeader(value = "Authorization", required = true) String authHeader) {
        return new JwtResponse(authService.refreshTokenFromHeader(authHeader));
    }

}
