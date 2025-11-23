package com.petconnect.chatbot.Infraestructure.driver_adapters.external_repository;

import com.petconnect.chatbot.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url:http://localhost:8181}")
    private String authServiceUrl;

    @Override
    public boolean usuarioExiste(String userId) {
        try {
            String url = authServiceUrl + "/api/petconnect/usuario/{userId}";
            restTemplate.getForEntity(url, Void.class, userId);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception error) {
            throw new RuntimeException("Error al consultar el servicio de usuarios", error);
        }
    }
}