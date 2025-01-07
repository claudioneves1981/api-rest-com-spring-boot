package dev.claudioneves.apirestdozero.service.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(){
        super("User not found");
    }
}
