package com.petconnect.pets.infraestructure.driver_adapters.external_repository;

import com.petconnect.pets.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final RestTemplate restTemplate;

    @Override
    public boolean usuarioExiste(Long userId) {
        try {
            restTemplate.getForEntity("http://localhost:9090/api/petconnect/usuario/" + userId, Void.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception errorMensaje) {
            throw new RuntimeException("Error al consultar el servicio de usuarios", errorMensaje);
        }
    }
}