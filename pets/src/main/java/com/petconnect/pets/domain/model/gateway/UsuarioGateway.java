package com.petconnect.pets.domain.model.gateway;

import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.dtos.UsuarioResponse;

public interface UsuarioGateway {

    boolean usuarioExiste(Long userId);
    boolean tieneRol(Long userId, String rol);
    UsuarioResponse obtenerUsuarioCompleto(Long userId);

}