package com.petconnect.auth.domain.model.gateway;
import com.petconnect.auth.domain.model.Usuario;
import java.time.LocalDateTime;

public interface UsuarioGateway {
    Usuario buscarPorId(Long id);
    void eliminarPorId(Long id);
    Usuario buscarPorEmail(String email);
    void actualizarLastLogin(Long id, LocalDateTime fecha);

}
