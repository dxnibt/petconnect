package com.petconnect.pets.infrastructure.driver_adapters.external_repository;

import com.petconnect.pets.domain.model.gateway.UsuarioGateway;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.dtos.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url:http://localhost:8181}")
    private String authServiceUrl;

    @Override
    public boolean usuarioExiste(Long userId) {
        try {
            String url = authServiceUrl + "/api/petconnect/usuario/" + userId;
            restTemplate.getForEntity(url, Void.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception error) {
            throw new RuntimeException("Error al consultar el servicio de usuarios", error);
        }
    }

    @Override
    public boolean tieneRol(Long userId, String rol) {
        try {
            String url = authServiceUrl + "/api/petconnect/usuario/" + userId;
            UsuarioResponse usuario = restTemplate.getForObject(url, UsuarioResponse.class);
            return usuario != null && rol.equals(usuario.getRole());
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception error) {
            throw new RuntimeException("Error al consultar el servicio de usuarios", error);
        }
    }

    @Override
    public UsuarioResponse obtenerUsuarioCompleto(Long userId) {
        try {
            String url = authServiceUrl + "/api/petconnect/usuario/" + userId;
            return restTemplate.getForObject(url, UsuarioResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception error) {
            throw new RuntimeException("Error al consultar el servicio de usuarios", error);
        }
    }
}