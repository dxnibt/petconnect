package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.exception.CamposIncompletosException;
import com.petconnect.auth.domain.exception.ContraseñaIncorrectaException;
import com.petconnect.auth.domain.exception.RefugioNoEncontradoException;
import com.petconnect.auth.domain.exception.UsuarioNoEncontradoException;
import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.usecase.UsuarioUseCase;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.AuthResponse;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.LoginDto;
import com.petconnect.auth.infraestructure.security.JwtValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petconnect/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final JwtValidation jwtValidation;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
        // Llamamos al caso de uso


        try{
            Usuario usuario = usuarioUseCase.loginUsuario(dto);
            // Generamos token (convertimos el rol de enum a String)
            String token = jwtValidation.generateToken(usuario.getId(), usuario.getRole().name());

            // Construimos la respuesta
            AuthResponse response = new AuthResponse(
                    usuario.getId(),
                    usuario.getEmail(),
                    usuario.getRole().name(),
                    token,
                    "Bienvenido");
            return ResponseEntity.ok(response);
        }catch (UsuarioNoEncontradoException | ContraseñaIncorrectaException | CamposIncompletosException error) {
            return ResponseEntity.badRequest().body(error.getMessage());

        }
    }


}
