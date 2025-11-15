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


    private final UsuarioMapper usuarioMapper;
    private final UsuarioUseCase usuarioUseCase;

    @PostMapping("/save")
    public ResponseEntity<?> saveUsuario(@RequestBody UsuarioData usuarioData) {

        try {
            Usuario usuarioConvertido = usuarioMapper.toUsuario(usuarioData);
            Usuario usuario = usuarioUseCase.guardarUsuario(usuarioConvertido);
            return new ResponseEntity<>(usuario, HttpStatus.OK);

        } catch (IllegalArgumentException error){
            return new ResponseEntity<>(error.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al guardar usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdUsuario(@PathVariable Long id){

        Usuario usuario = usuarioUseCase.buscarUsuarioPorId(id);

        if(usuario.getId() != null){
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id) {
        try {
            usuarioUseCase.eliminarUsuarioPorId(id);
            return new ResponseEntity<>("Usuario eliminado", HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> actualizarUsuario(@RequestBody UsuarioData usuarioData) {
        try {
            Usuario usuario = usuarioMapper.toUsuario(usuarioData);
            Usuario usuarioValidadoActualizado = usuarioUseCase.actualizarUsuario(usuario);
            return new  ResponseEntity<>(usuarioValidadoActualizado, HttpStatus.OK);
            // }catch(Exception error){
            //   return ResponseEntity.notFound().build();
        }catch (Exception error){
            return new ResponseEntity<>(error.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioData usuarioData) {
        try {
            Usuario usuario = usuarioMapper.toUsuario(usuarioData);
            String mensaje = usuarioUseCase.loginUsuario(usuario);
            return new ResponseEntity<>(mensaje , HttpStatus.OK);
        } catch (RuntimeException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.OK);
        }
    }

}
