package com.petconnect.auth.infraestructure.security;

import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncrypterGatewayImpl  implements EncrypterGateway {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encrypt(String password) {
        return encoder.encode(password);
    }

    @Override
    public Boolean checkPassword(String passUser, String passBD) {
        return encoder.matches(passUser, passBD);
    }
}
