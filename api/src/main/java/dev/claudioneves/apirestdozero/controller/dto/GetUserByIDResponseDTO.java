package dev.claudioneves.apirestdozero.controller.dto;

import java.util.UUID;

public record GetUserByIDResponseDTO(

        UUID id,
        String name,
        String email,
        String createdAt,
        String updatedAt
) {
}
