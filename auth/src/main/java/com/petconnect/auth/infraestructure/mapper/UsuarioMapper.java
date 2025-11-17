package com.petconnect.auth.infraestructure.mapper;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toUsuario(UsuarioData usuarioData){
        if (usuarioData == null) return null;
        return new Usuario(
                usuarioData.getId(),
                usuarioData.getName(),
                usuarioData.getEmail(),
                usuarioData.getPassword(),
                usuarioData.getPhoneNumber(),
                usuarioData.getCity(),
                usuarioData.getAddress(),
                usuarioData.getProfilePicture(),
                usuarioData.getRole(),
                usuarioData.getRegistrationDate(),
                usuarioData.getLastLogin()
        );
    }

    public UsuarioData toData(Usuario usuario){
        if (usuario == null) return null;
        return new UsuarioData(
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getPhoneNumber(),
                usuario.getCity(),
                usuario.getAddress(),
                usuario.getProfilePicture(),
                usuario.getRole(),
                usuario.getRegistrationDate(),
                usuario.getLastLogin()
        );
    }

}
