package dev.claudioneves.apirestdozero.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticateUserResponseDTO(

        @Schema(
                description = "Access token",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZUB0ZXN0ZTIuY29tLmJyIiwiaWF0IjoxNzM2MDM2NzcyLCJleHAiOjE3MzYwMzcxMzJ9.jkkT6imh_GwNgKyqFy32iI3npwkn7hS8hG7xOQR1iPs"
        )
        String accessToken
) {
}
