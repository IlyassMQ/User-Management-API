package org.example.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
    public InvalidCredentialsException(){
        super("Email or Password incorrect");
    }
}
