package com.petconnect.auth.domain.model.gateway;
import com.petconnect.auth.domain.model.Usuario;

public interface UsuarioGateway {

    Usuario buscarPorId(Long id);
    void eliminarPorId(Long id);
    String loginUsuario(Usuario usuario);

}
