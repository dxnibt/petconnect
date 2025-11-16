package com.petconnect.pets.domain.exception;

public class MascotaNoEncontradaException extends RuntimeException {
    public MascotaNoEncontradaException(String message) {
        super(message);
    }
}
