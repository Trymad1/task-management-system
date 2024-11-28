package com.trymad.task_management.web.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contain valid jwt token")
public record JwtResponse(String token) {
    
}
