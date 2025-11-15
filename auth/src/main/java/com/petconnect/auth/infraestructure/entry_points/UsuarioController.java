package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.usecase.UsuarioUseCase;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioData;
import com.petconnect.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petconnect/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdUsuario(@PathVariable Long id) {

        Usuario usuario = usuarioUseCase.buscarUsuarioPorId(id);

        if (usuario.getId() != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    }
}
