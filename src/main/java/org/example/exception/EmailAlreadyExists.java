package org.example.exception;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String message) {
        super(message);
    }
    public EmailAlreadyExists(){
        super("Email Already Exist");
    }
}
