package com.petconnect.pets.domain.exception;

public class UsuarioNoAdoptanteException extends RuntimeException {
    public UsuarioNoAdoptanteException(String message) {
        super(message);
    }
}
