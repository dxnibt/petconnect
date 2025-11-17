package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petconnect/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdUsuario(@PathVariable Long id) {

        Usuario usuario = usuarioUseCase.buscarUsuarioPorId(id);

        if (usuario.getId() != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }

        return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id) {

        try {
            usuarioUseCase.eliminarUsuario(id);
            return new ResponseEntity<>("Usuario eliminado", HttpStatus.OK);

        } catch (RuntimeException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
