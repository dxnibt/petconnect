package com.petconnect.pets.domain.model.gateway;

public interface UsuarioGateway {

    boolean usuarioExiste(Long userId);
    boolean tieneRol(Long userId, String rol);

}