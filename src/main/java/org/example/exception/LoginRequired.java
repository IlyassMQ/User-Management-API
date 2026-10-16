package org.example.exception;

public class LoginRequired extends RuntimeException {
    public LoginRequired(String message) {
        super(message);
    }

    public LoginRequired() {
        super("You must be logged in");
    }
}
