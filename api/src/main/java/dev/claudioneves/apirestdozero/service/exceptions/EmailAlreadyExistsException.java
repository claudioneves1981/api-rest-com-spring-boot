package dev.claudioneves.apirestdozero.service.exceptions;


public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(){
        super("Email already exist");
    }
}
