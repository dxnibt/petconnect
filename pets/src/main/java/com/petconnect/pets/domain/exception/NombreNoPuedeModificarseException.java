package com.petconnect.pets.domain.exception;

public class NombreNoPuedeModificarseException extends RuntimeException {
    public NombreNoPuedeModificarseException(String message) {
        super(message);
    }
}
