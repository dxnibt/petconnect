package com.petconnect.pets.domain.model.gateway;

public interface RefugioGateway {
    void restarMascota(Long refugioId);
    boolean refugioExiste(Long refugioId);
}
