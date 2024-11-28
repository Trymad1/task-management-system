package com.trymad.task_management.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trymad.task_management.exception.JwtErrorResponseWriter;
import com.trymad.task_management.security.AuthenticationService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AuthenticationService authService;
    private final JwtErrorResponseWriter exceptionWriter;

    private static final List<String> NO_AUTH_PATH = Arrays.asList("/auth/login", "/auth/registry", "/auth/refresh");

    private final String INVALID_SIGNATURE = "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String pathRequest = request.getRequestURI();
        if (NO_AUTH_PATH.contains(pathRequest)) {
            doFilter(request, response, filterChain);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                authService.authenticate(authHeader.substring(7));
            } catch (ExpiredJwtException e) {
                exceptionWriter.invalidToken(request, response, e);
            } catch (MalformedJwtException e) {
                exceptionWriter.invalidToken(request, response, new JwtException(INVALID_SIGNATURE));
            } catch (JwtException e) {
                exceptionWriter.invalidToken(request, response, e);
            }

        } else if (authHeader == null) {
            exceptionWriter.requiredToken(request, response);
        }

        filterChain.doFilter(request, response);
    }

}
