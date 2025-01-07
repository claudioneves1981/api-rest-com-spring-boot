package dev.claudioneves.apirestdozero.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CreateUserResponseDTO(

        @Schema(description = "User id")
        UUID id

) { }
