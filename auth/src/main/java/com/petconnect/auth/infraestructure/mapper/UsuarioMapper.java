package com.petconnect.auth.infraestructure.mapper;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {


    public Usuario toUsuario(UsuarioData usuarioData){

        return new Usuario(
                usuarioData.getId(),
                usuarioData.getNombre(),
                usuarioData.getEmail(),
                usuarioData.getPassword(),
                usuarioData.getFechaNacimiento(),
                usuarioData.getTelefono(),
                usuarioData.getCiudad(),
                usuarioData.getDireccion(),
                usuarioData.getFotoPerfil(),
                usuarioData.getRole()
        );
    }

    public UsuarioData toData(Usuario usuario){

        return new UsuarioData(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getFechaNacimiento(),
                usuario.getTelefono(),
                usuario.getCiudad(),
                usuario.getDireccion(),
                usuario.getFotoPerfil(),
                usuario.getRole()
        );
    }

}
