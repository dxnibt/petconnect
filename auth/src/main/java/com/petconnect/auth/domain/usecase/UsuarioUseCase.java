package com.petconnect.auth.domain.usecase;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.model.gateway.AdoptanteGateway;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.RefugioGateway;
import com.petconnect.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

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
