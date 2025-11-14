package com.petconnect.auth.domain.usecase;

import com.petconnect.auth.domain.model.Refugio;

import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.RefugioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RefugioUseCase {

    private final RefugioGateway refugioGateway;
    private final EncrypterGateway encrypterGateway;


    public Refugio guardarRefugio(Refugio refugio) {

        if (!refugio.isValid()) {
            throw new IllegalArgumentException("Por favor complete todos los campos");
        }
        String passwordEncrypt = encrypterGateway.encrypt(refugio.getPassword());
        refugio.setPassword(passwordEncrypt);
        return refugioGateway.guardarRefugio(refugio);

    }

}
