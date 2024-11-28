package com.trymad.task_management.web.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trymad.task_management.exception.JwtErrorResponseWriter;
import com.trymad.task_management.security.AuthenticationService;
import com.trymad.task_management.security.PublicPathStorage;

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
    private final PublicPathStorage publicPaths;

    private final String INVALID_SIGNATURE = "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (publicPaths.isPublicPath(request)) {
            doFilter(request, response, filterChain);
            return;
        }
         
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                authService.authenticate(authHeader.substring(7));
            } catch (ExpiredJwtException e) {
                exceptionWriter.invalidToken(request, response, e);
                return;
            } catch (MalformedJwtException e) {
                exceptionWriter.invalidToken(request, response, new JwtException(INVALID_SIGNATURE));
                return;
            } catch (JwtException e) {
                exceptionWriter.invalidToken(request, response, e);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

}
