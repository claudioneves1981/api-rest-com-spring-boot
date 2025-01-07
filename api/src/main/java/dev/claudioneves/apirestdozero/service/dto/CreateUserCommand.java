package dev.claudioneves.apirestdozero.service.dto;

public record CreateUserCommand (

        String name,
        String email,
        String password
){
}
