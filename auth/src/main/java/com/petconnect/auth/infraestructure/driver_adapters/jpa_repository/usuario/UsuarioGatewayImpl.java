package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.UsuarioGateway;
import com.petconnect.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {


    @Override
    public String loginUsuario(Usuario usuario) {
        return "";
    }
}

