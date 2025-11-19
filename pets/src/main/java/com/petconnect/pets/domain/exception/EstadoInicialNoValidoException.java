package com.petconnect.pets.domain.exception;

public class EstadoInicialNoValidoException extends RuntimeException {
    public EstadoInicialNoValidoException(String message) {
        super(message);
    }
}
