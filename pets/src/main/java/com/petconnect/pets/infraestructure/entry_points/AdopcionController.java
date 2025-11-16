package com.petconnect.pets.infraestructure.entry_points;

import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.usecase.AdopcionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petconnect/adopciones")
@RequiredArgsConstructor
public class AdopcionController {

    private final AdopcionUseCase useCase;

    // Crear solicitud de adopción
    @PostMapping("/save")
    public ResponseEntity<?> crear(@RequestBody Adopcion adopcion) {
        try {
            Adopcion creada = useCase.crear(adopcion);
            return new ResponseEntity<>(creada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Adopcion adopcion = useCase.obtenerPorId(id);

        if (adopcion == null) {
            return new ResponseEntity<>("La solicitud de adopción no existe", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(adopcion, HttpStatus.OK);
    }

    // Obtener por usuario
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long userId) {
        Adopcion adopcion = useCase.obtenerPorUserId(userId);

        if (adopcion == null) {
            return new ResponseEntity<>("El usuario no tiene solicitudes de adopción", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(adopcion, HttpStatus.OK);
    }

    // Aceptar solicitud
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<?> aceptar(@PathVariable Long id) {
        try {
            Adopcion aceptada = useCase.aceptar(id);
            return new ResponseEntity<>(aceptada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Rechazar solicitud
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable Long id) {
        try {
            Adopcion rechazada = useCase.rechazar(id);
            return new ResponseEntity<>(rechazada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar solicitud
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            useCase.eliminar(id);
            return new ResponseEntity<>("Solicitud de adopción eliminada", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
