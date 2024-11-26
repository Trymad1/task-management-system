package com.trymad.task_management.security;

import org.springframework.stereotype.Service;

import com.trymad.task_management.security.jwt.JwtTokenProvider;
import com.trymad.task_management.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Service
public class AuthenticationService {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

}
