package com.petconnect.pets.infrastructure.entry_points;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.petconnect.pets.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MascotaNoEncontradaException.class)
    public ResponseEntity<?> handleMascotaNotFound(MascotaNoEncontradaException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(EstadoInicialNoValidoException.class)
    public ResponseEntity<?> handleInicialState(EstadoInicialNoValidoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NombreNoPuedeModificarseException.class)
    public ResponseEntity<?> handleName(NombreNoPuedeModificarseException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(FechaDeNacimientoObligatoriaException.class)
    public ResponseEntity<?> handleFechaNotNull(FechaDeNacimientoObligatoriaException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(FechaDeNacimientoInvalidaException.class)
    public ResponseEntity<?> handleInvalidDate(FechaDeNacimientoInvalidaException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(EdadNoPermitidaException.class)
    public ResponseEntity<?> handleInvalidAge(EdadNoPermitidaException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ActualizacionEsterilizacionInvalidaException.class)
    public ResponseEntity<?> handleInvalidSterilization(ActualizacionEsterilizacionInvalidaException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ActualizacionEstadoInvalidoException.class)
    public ResponseEntity<?> handleInvalidState(ActualizacionEstadoInvalidoException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
    @ExceptionHandler(UsuarioNoAutenticadoException.class)
    public ResponseEntity<?> usuarioNoAutenticado(UsuarioNoAutenticadoException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
    @ExceptionHandler(BloqueoDePermisosException.class)
    public ResponseEntity<?> bloqueoDePermisos(BloqueoDePermisosException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // Handler único para HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof UnrecognizedPropertyException e) {
            String campo = e.getPropertyName();
            String mensaje = "El campo '" + campo + "' no está permitido para actualización.";
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "CAMPO_NO_PERMITIDO",
                            "message", mensaje,
                            "timestamp", LocalDateTime.now()
                    ));
        }

        // Otros errores de parsing (ej: fecha inválida)
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Formato de datos inválido. Verifique el JSON y el formato de fecha (dd/MM/yyyy).");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "VALIDACION_FALLIDA",
                        "fields", errores,
                        "timestamp", LocalDateTime.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor.");
    }
}
