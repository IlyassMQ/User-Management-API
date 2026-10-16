package org.example.exception;

public class AcounntNotActive extends RuntimeException {
    public AcounntNotActive(String message) {
        super(message);
    }

    public AcounntNotActive() {
        super("Account is desactivated");
    }
}
