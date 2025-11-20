package com.petconnect.auth.domain.model.gateway;
import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioData;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UsuarioGateway {
    Usuario buscarPorId(Long id);
    void eliminarPorId(Long id);
    Usuario buscarPorEmail(String email);
    void actualizarLastLogin(Long id, LocalDateTime fecha);

}
