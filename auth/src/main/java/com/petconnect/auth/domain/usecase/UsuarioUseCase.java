package com.petconnect.auth.domain.usecase;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;

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

}
