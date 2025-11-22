package com.petconnect.chatbot.Infraestructure.driver_adapters.external_repository;

import com.petconnect.chatbot.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final RestTemplate restTemplate;

    @Override
    public boolean usuarioExiste(String userId) {
        try {
            restTemplate.getForEntity(
                    "http://localhost:8181/api/petconnect/usuario/{userId}",
                    Void.class,
                    userId
            );
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception error) {
            throw new RuntimeException("Error al consultar el servicio de usuarios", error);
        }
    }
}