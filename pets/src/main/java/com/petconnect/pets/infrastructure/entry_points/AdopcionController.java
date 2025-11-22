package com.petconnect.pets.infrastructure.entry_points;

import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.usecase.AdopcionUseCase;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.JwtDto.JwtUserDetails;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.dtos.AdopcionDetalladaResponse;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.dtos.SolicitudAdopcionRequest;
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
            Adopcion adopcionCreada = useCase.crear(userDetails.getId(), solicitud.getPetId());
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

    @PatchMapping("/{id}/aceptar")
    public ResponseEntity<?> aceptarAdopcion(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("Token inválido", HttpStatus.UNAUTHORIZED);
        }

        try {
            Adopcion aceptada = useCase.aceptarAdopcion(id, userDetails.getId());
            return new ResponseEntity<>(aceptada, HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarAdopcion(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("Token inválido", HttpStatus.UNAUTHORIZED);
        }

        try {
            Adopcion rechazada = useCase.rechazarAdopcion(id, userDetails.getId());
            return new ResponseEntity<>(rechazada, HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarSolicitud(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("Token inválido", HttpStatus.UNAUTHORIZED);
        }

        try {
            useCase.eliminarSolicitudUsuario(id, userDetails.getId());
            return new ResponseEntity<>("Solicitud de adopción eliminada", HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mis-solicitudes")
    public ResponseEntity<?> obtenerMisSolicitudes(
            @RequestParam(defaultValue = "-1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("Token inválido", HttpStatus.UNAUTHORIZED);
        }

        try {
            List<Adopcion> solicitudes = useCase.obtenerTodasSolicitudesUsuario(userDetails.getId(), page, size);

            if (solicitudes.isEmpty()) {
                return new ResponseEntity<>("No solicitudes nuevas", HttpStatus.OK);
            }

            return new ResponseEntity<>(solicitudes, HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mi-solicitud-actual")
    public ResponseEntity<?> obtenerMiSolicitudActual(@AuthenticationPrincipal JwtUserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>("Token inválido", HttpStatus.UNAUTHORIZED);
        }

        try {
            Adopcion adopcion = useCase.obtenerSolicitudUsuario(userDetails.getId());

            if (adopcion == null) {
                return new ResponseEntity<>("No tienes solicitudes de adopción en proceso", HttpStatus.OK);
            }

            return new ResponseEntity<>(adopcion, HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/refugio/detalles/{id}")
    public ResponseEntity<?> obtenerDetallesAdopcionRefugio(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>("Token inválido", HttpStatus.UNAUTHORIZED);
        }

        try {
            AdopcionDetalladaResponse detalles = useCase.obtenerDetallesCompletosParaRefugio(id, userDetails.getId());
            return new ResponseEntity<>(detalles, HttpStatus.OK);

        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
