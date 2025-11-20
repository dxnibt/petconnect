package com.petconnect.pets.domain.exception;

public class UsuarioNoAutenticadoException extends RuntimeException {
    public UsuarioNoAutenticadoException(String message) {
        super(message);
    }
}
