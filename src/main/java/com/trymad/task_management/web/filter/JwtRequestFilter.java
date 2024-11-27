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

    private static final List<String> NOT_REQUIRED_TOKEN = Arrays.asList("/auth/login", "/auth/registry");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String pathRequest = request.getRequestURI();
        final boolean OPEN_ENDPOINT = NOT_REQUIRED_TOKEN.contains(pathRequest);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                authService.authenticate(authHeader.substring(7));
            } catch (ExpiredJwtException e) {
                if (!OPEN_ENDPOINT)
                    exceptionWriter.expiredToken(request, response);
            } catch (JwtException e) {
                if (!OPEN_ENDPOINT)
                    exceptionWriter.invalidToken(request, response);
            } catch (Exception e) {
                if (!OPEN_ENDPOINT)
                    exceptionWriter.invalidToken(request, response);
            }
        }

        if (!OPEN_ENDPOINT && authHeader == null) {
            exceptionWriter.requiredToken(request, response);
        }

        filterChain.doFilter(request, response);
    }

}
