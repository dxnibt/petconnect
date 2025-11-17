package com.petconnect.auth.domain.usecase;

import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.RefugioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RefugioUseCase {

    private final RefugioGateway refugioGateway;
    private final EncrypterGateway encrypterGateway;
    private final UsuarioUseCase usuarioUseCase;

    public Refugio guardarRefugio(Refugio refugio) {

        if (!usuarioUseCase.isValidUsuario(refugio) || !isValidRefugio(refugio)) {
            throw new IllegalArgumentException("Por favor, complete todos los campos");
        }

        String passwordEncrypt = encrypterGateway.encrypt(refugio.getPassword());
        refugio.setPassword(passwordEncrypt);
        return refugioGateway.guardarRefugio(refugio);

    }

    private boolean isValidRefugio(Refugio refugio){
        return  refugio.getNit() != null &&
                refugio.getWebsite() != null &&
                refugio.getSupportDocument() != null &&
                refugio.getShelterDescription() != null;
    }
}
