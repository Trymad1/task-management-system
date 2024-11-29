package com.trymad.task_management.service;

import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.trymad.task_management.model.Role;
import com.trymad.task_management.model.User;
import com.trymad.task_management.web.dto.user.LoginDTO;
import com.trymad.task_management.web.dto.user.UserCreateDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Service
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authManager;
    private final UserService userService;

    public String createJwtToken(LoginDTO loginDTO) {
        final UserDetails userDetails = (UserDetails) authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.mail(), loginDTO.password())).getPrincipal();

        return jwtTokenProvider.generateToken(userDetails);
    }

    public Authentication authenticate(String token) {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                jwtTokenProvider.getMail(token),
                null,
                jwtTokenProvider.getRoles(token).stream().map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toSet()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public String registryUser(UserCreateDTO userCreateDTO) {
        final User user = userService.create(userCreateDTO, Role.USER);
        return jwtTokenProvider.generateToken(userService.toUserDetails(user));
    }

    public String refreshTokenFromHeader(String authHeader) {
        return jwtTokenProvider.refresh(authHeader.substring(7));
    }
}
