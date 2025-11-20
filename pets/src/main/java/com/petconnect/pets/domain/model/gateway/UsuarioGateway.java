package com.petconnect.pets.domain.model.gateway;

import com.petconnect.pets.domain.model.Mascota;

public interface UsuarioGateway {
    boolean usuarioExiste(Long userId);
    boolean tieneRol(Long userId, String rol);
}