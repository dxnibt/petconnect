package com.petconnect.auth.domain.exception;

public class ContraseñaIncorrectaException extends RuntimeException {
    public ContraseñaIncorrectaException(String message) {
        super(message);
    }
}
