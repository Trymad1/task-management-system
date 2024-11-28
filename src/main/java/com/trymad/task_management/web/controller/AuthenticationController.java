package com.trymad.task_management.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.trymad.task_management.service.AuthenticationService;
import com.trymad.task_management.web.dto.jwt.JwtResponse;
import com.trymad.task_management.web.dto.user.LoginDTO;
import com.trymad.task_management.web.dto.user.UserCreateDTO;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Login, for claim jwt token", description = "By default, the token is valid for 30 minutes")
    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.OK)
    public JwtResponse createTokenByCredentionals(@RequestBody @Valid LoginDTO loginDTO) {
        return new JwtResponse(authService.createJwtToken(loginDTO));
    }

    @Operation(summary = "Registration the new user, claim jwt token", description = "Each user must have a unique email")
    @PostMapping("registry")
    @ResponseStatus(value = HttpStatus.CREATED)
    public JwtResponse registy(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        return new JwtResponse(authService.registryUser(userCreateDTO));
    }

    @Operation(summary = "Refresh token, claim new jwt token", description = "Token must be in header, valid and not expired")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("refresh")
    @ResponseStatus(value = HttpStatus.OK)
    public JwtResponse refreshToken(@RequestHeader(value = "Authorization", required = true) String authHeader) {
        return new JwtResponse(authService.refreshTokenFromHeader(authHeader));
    }

}
