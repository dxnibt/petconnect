package com.petconnect.pets.infrastructure.entry_points;

import com.petconnect.pets.domain.exception.*;
import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.usecase.AdopcionUseCase;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.JwtDto.JwtUserDetails;
import com.petconnect.pets.infrastructure.dtos.SolicitudAdopcionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/petconnect/adopciones")
@RequiredArgsConstructor
public class AdopcionController {

    private final AdopcionUseCase useCase;

    // Crear solicitud de adopción
    @PostMapping("/save")
    public ResponseEntity<?> solicitarAdopcion(
            @RequestBody SolicitudAdopcionRequest solicitud,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("Token inválido o sin roles", HttpStatus.UNAUTHORIZED);
        }

        try {
            // Crear objeto Adopcion con datos del JWT y request
            Adopcion adopcion = new Adopcion();
            adopcion.setUserId(userDetails.getId()); // Del token
            adopcion.setPetId(solicitud.getPetId()); // Del body

            // El UseCase se encarga de validar si es adoptante
            Adopcion adopcionCreada = useCase.crear(adopcion);
            return new ResponseEntity<>(adopcionCreada, HttpStatus.OK);

        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/refugio/solicitudes")
    public ResponseEntity<?> obtenerSolicitudesRefugio(
            @RequestParam(defaultValue = "-1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("Token inválido", HttpStatus.UNAUTHORIZED);
        }

        try {
            List<Adopcion> adopciones = useCase.obtenerPorShelterId(userDetails.getId(), page, size);

            if (adopciones.isEmpty()) {
                return new ResponseEntity<>("No hay más solicitudes de adopción para tu refugio", HttpStatus.OK);
            }

            return new ResponseEntity<>(adopciones, HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    // Obtener por ID
//    @GetMapping("/{id}")
//    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
//        Adopcion adopcion = useCase.obtenerPorId(id);
//
//        if (adopcion == null) {
//            return new ResponseEntity<>("La solicitud de adopción no existe", HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<>(adopcion, HttpStatus.OK);
//    }
//
//    // Obtener por usuario
//    @GetMapping("/usuario/{userId}")
//    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long userId) {
//        Adopcion adopcion = useCase.obtenerPorUserId(userId);
//
//        if (adopcion == null) {
//            return new ResponseEntity<>("El usuario no tiene solicitudes de adopción", HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<>(adopcion, HttpStatus.OK);
//    }
//
//    // Aceptar solicitud
//    @PutMapping("/{id}/aceptar")
//    public ResponseEntity<?> aceptar(@PathVariable Long id) {
//        try {
//            Adopcion aceptada = useCase.aceptar(id);
//            return new ResponseEntity<>(aceptada, HttpStatus.OK);
//        } catch (AdopcionNoEncontradaException | MascotaNoDisponibleException | MascotaNoEncontradaException |
//                 ErrorRefugioException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Rechazar solicitud
//    @PutMapping("/{id}/rechazar")
//    public ResponseEntity<?> rechazar(@PathVariable Long id) {
//        try {
//            Adopcion rechazada = useCase.rechazar(id);
//            return new ResponseEntity<>(rechazada, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    // Eliminar solicitud
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> eliminar(@PathVariable Long id) {
//        try {
//            useCase.eliminar(id);
//            return new ResponseEntity<>("Solicitud de adopción eliminada", HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
}
