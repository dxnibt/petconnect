package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.exception.RefugioNoEncontradoException;
import com.petconnect.auth.domain.usecase.AdminUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/petconnect/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUseCase adminUseCase;

    @GetMapping("/refugios/pendientes")
    public ResponseEntity<?> listarRefugiosPendientes() {

        try {
            return ResponseEntity.ok(adminUseCase.listarRefugiosPendientes());

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