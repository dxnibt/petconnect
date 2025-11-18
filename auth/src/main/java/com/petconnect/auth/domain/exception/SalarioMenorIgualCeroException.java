package com.petconnect.auth.domain.exception;

public class SalarioMenorIgualCeroException extends RuntimeException {
    public SalarioMenorIgualCeroException(String message) {
        super(message);
    }
}
