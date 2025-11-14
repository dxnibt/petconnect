package com.petconnect.pets.domain.exception;

public class MascotaExisteException extends RuntimeException {
    public MascotaExisteException(String message) {
        super(message);
    }
}
