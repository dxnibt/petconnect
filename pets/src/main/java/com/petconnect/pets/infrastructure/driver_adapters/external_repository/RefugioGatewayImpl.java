package com.petconnect.pets.infrastructure.driver_adapters.external_repository;

import com.petconnect.pets.domain.model.gateway.RefugioGateway;
import org.springframework.stereotype.Repository;

@Repository
public class RefugioGatewayImpl implements RefugioGateway {

    @Override
    public void restarMascota(Long refugioId) {
        // Temporalmente vacío
    }

    @Override
    public boolean refugioExiste(Long refugioId) {
        return true; // Temporalmente siempre true
    }
}