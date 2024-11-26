package com.trymad.task_management.security;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trymad.task_management.security.jwt.JwtResponse;
import com.trymad.task_management.security.jwt.LoginDTO;
import com.trymad.task_management.web.dto.user.UserCreateDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("login")
    public JwtResponse createTokenByCredentionals(@RequestBody LoginDTO loginDTO) {
        return new JwtResponse(authService.createJwtToken(loginDTO));
    }

    @PostMapping("registry")
    public JwtResponse registy(@RequestBody UserCreateDTO userCreateDTO) {
        return new JwtResponse(authService.registryUser(userCreateDTO));
    }

    @PostMapping("refresh")
    public JwtResponse refreshToken(@RequestHeader(value = "Authorization", required = true) String authHeader) {
        return new JwtResponse(authService.refreshTokenFromHeader(authHeader));
    }

}
