package com.trymad.task_management.web.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trymad.task_management.security.AuthenticationService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Component

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AuthenticationService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                authService.authenticate(authHeader.substring(7));
            } catch (ExpiredJwtException e) {
                log.info("Token expired");
            } catch (SignatureException e) {
                log.info("Invalid token signature");
            } catch (JwtException e) {
                log.info("Incaused jwt exception");
            }
        }

        filterChain.doFilter(request, response);
    }

}
