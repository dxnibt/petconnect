package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.domain.usecase.AdminUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/petconnect/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUseCase adminUseCase;

    @GetMapping("/refugios/pendientes")
    public ResponseEntity<List<Refugio>> listarRefugiosPendientes() {
        return ResponseEntity.ok(adminUseCase.listarRefugiosPendientes());
    }

    @PutMapping("/refugios/aprobar/{id}")
    public ResponseEntity<Refugio> aprobarRefugio(@PathVariable Long id) {
        return ResponseEntity.ok(adminUseCase.aprobarRefugio(id));
    }
}
