package com.trymad.task_management.util;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.trymad.task_management.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ErrorResponseSupplyer {
    
    public ResponseEntity<ErrorResponse> getResponse(HttpStatus status, HttpServletRequest request, String message) {
        final String path = request.getRequestURI();
        final Instant timestamp = Instant.now();
        final String method = request.getMethod();

        final ErrorResponse response = new ErrorResponse(status.value(), message, path, method, timestamp);
        return new ResponseEntity<ErrorResponse>(response, status);
    }

}
