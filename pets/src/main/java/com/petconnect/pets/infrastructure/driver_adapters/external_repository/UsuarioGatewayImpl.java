package com.petconnect.pets.infrastructure.driver_adapters.external_repository;

import com.petconnect.pets.domain.model.gateway.UsuarioGateway;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.dtos.UsuarioResponse;
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
            restTemplate.getForEntity("http://localhost:8181/api/petconnect/usuario/" + userId, Void.class);
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
            UsuarioResponse usuario = restTemplate.getForObject(
                    "http://localhost:8181/api/petconnect/usuario/" + userId,
                    UsuarioResponse.class
            );
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
            return restTemplate.getForObject(
                    "http://localhost:8181/api/petconnect/usuario/" + userId,
                    UsuarioResponse.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception error) {
            throw new RuntimeException("Error al consultar el servicio de usuarios", error);
        }
    }
}