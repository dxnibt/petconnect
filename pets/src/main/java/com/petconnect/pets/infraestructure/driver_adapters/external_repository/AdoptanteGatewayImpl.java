package com.petconnect.pets.infraestructure.driver_adapters.external_repository;

import com.petconnect.pets.domain.model.gateway.AdoptanteGateway;
import com.petconnect.pets.infraestructure.dtos.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class AdoptanteGatewayImpl implements AdoptanteGateway {

    private final RestTemplate restTemplate;

    @Override
    public boolean esAdoptante(Long userId) {
        try {
            UsuarioResponse usuario = restTemplate.getForObject(
                    "http://localhost:9090/api/petconnect/usuario/" + userId,
                    UsuarioResponse.class
            );
            return usuario != null && "ADOPTANTE".equals(usuario.getRole());
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception errorMensaje) {
            throw new RuntimeException("Error al consultar el servicio", errorMensaje);
        }
    }
}