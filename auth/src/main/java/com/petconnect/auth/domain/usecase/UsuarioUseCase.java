package com.petconnect.auth.domain.usecase;

import com.petconnect.auth.domain.exception.CamposIncompletosException;
import com.petconnect.auth.domain.exception.ContraseñaIncorrectaException;
import com.petconnect.auth.domain.exception.UsuarioNoEncontradoException;
import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.model.gateway.AdoptanteGateway;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.RefugioGateway;
import com.petconnect.auth.domain.model.gateway.UsuarioGateway;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.LoginDto;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioActualizarDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;
    private final RefugioGateway refugioGateway;
    private final AdoptanteGateway adoptanteGateway;

    public Usuario buscarUsuarioPorId(Long id){
        try {
            Usuario usuario = usuarioGateway.buscarPorId(id);

            switch (usuario.getRole()) {
                case REFUGIO:
                    return refugioGateway.buscarPorId(id);
                case ADOPTANTE:
                    return adoptanteGateway.buscarPorId(id);
                default:
                    return usuario;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Usuario();
        }
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioGateway.buscarPorId(id);

        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }

        switch (usuario.getRole()) {
            case REFUGIO:
                refugioGateway.eliminarPorId(id);
            case ADOPTANTE:
                adoptanteGateway.eliminarPorId(id);

        }
    }

    public Usuario loginUsuario(LoginDto dto){

        if (dto.getEmail() == null || dto.getPassword() == null) {
            throw new CamposIncompletosException("El email y la contraseña son obligatorios");
        }

        Usuario usuario = usuarioGateway.buscarPorEmail(dto.getEmail());
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }

        boolean passwordValid = encrypterGateway.checkPassword(dto.getPassword(), usuario.getPassword());
        if (!passwordValid) {
            throw new ContraseñaIncorrectaException("Contraseña incorrecta");
        }

        usuarioGateway.actualizarLastLogin(usuario.getId(), LocalDateTime.now());

        return usuario; // ahora devolvemos el objeto completo
    }


    public boolean isValidUsuario(Usuario usuario) {
        return usuario.getName() != null &&
                usuario.getEmail() != null &&
                usuario.getPassword() != null &&
                usuario.getPhoneNumber() != null &&
                usuario.getCity() != null &&
                usuario.getAddress() != null &&
                usuario.getProfilePicture() != null &&
                usuario.getRole() != null;
    }

    public void actualizarUsuarioDto(Usuario usuario, UsuarioActualizarDto dto){
        if (dto != null) {
            if (dto.getEmail() != null) usuario.setEmail(dto.getEmail());
            if (dto.getPassword() != null) {
                String passwordEncrypt = encrypterGateway.encrypt(dto.getPassword());
                usuario.setPassword(passwordEncrypt);
            }
            if (dto.getPhoneNumber() != null) usuario.setPhoneNumber(dto.getPhoneNumber());
            if (dto.getCity() != null) usuario.setCity(dto.getCity());
            if (dto.getAddress() != null) usuario.setAddress(dto.getAddress());
            if (dto.getProfilePicture() != null) usuario.setProfilePicture(dto.getProfilePicture());
        }
    }



}
