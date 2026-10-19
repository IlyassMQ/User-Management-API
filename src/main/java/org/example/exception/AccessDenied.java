package org.example.exception;

public class AccessDenied extends RuntimeException {
    public AccessDenied(String message) {
        super(message);
    }

    public AccessDenied() {
        super("Acces denied");
    }
}
