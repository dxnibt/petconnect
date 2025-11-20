package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.exception.RefugioNoEncontradoException;
import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.domain.usecase.AdminUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/petconnect/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUseCase adminUseCase;

    @GetMapping("/refugios/pendientes")
    public ResponseEntity<?> listarRefugiosPendientes(
            @RequestParam(defaultValue = "-1") int page,
            @RequestParam(defaultValue = "2") int size) {

        try {
            List<Refugio> refugios = adminUseCase.listarRefugiosPendientes(page, size);
            if (refugios.isEmpty()) {
                // Mensaje amigable cuando se termina la lista de refugios pendientes
                return ResponseEntity.ok("No hay más refugios pendientes de aprobación");
            }
            return ResponseEntity.ok(refugios);

        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        }
    }


    @PutMapping("/refugios/aprobar/{id}")
    public ResponseEntity<?> aprobarRefugio(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(adminUseCase.aprobarRefugio(id));

        } catch (RefugioNoEncontradoException error) {
            return ResponseEntity.status(HttpStatus.OK).body(error.getMessage());
        }

    }
}