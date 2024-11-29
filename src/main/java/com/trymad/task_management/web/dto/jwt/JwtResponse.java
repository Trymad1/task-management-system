package com.trymad.task_management.web.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contain valid jwt token")
public record JwtResponse(

        @Schema(example = "eyJhbGciOiJIUzM4NCJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6Ik9sZWdAZ21haWwuY29tIiwiaWF0IjoxNzMyODM4NDQxLCJleHAiOjE3MzI4NDAyNDF9.h5KJEelz74_z1yn4RgQb4dAflSWGH-2A5zCynlLH2h8OKK7Q3g8BhFhN7JoiooEy") String token) {

}
