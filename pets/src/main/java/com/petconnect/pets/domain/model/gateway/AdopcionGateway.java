package com.petconnect.pets.domain.model.gateway;

import com.petconnect.pets.domain.model.Adopcion;

public interface AdopcionGateway {
    Adopcion crear(Adopcion adopcion);
    Adopcion buscarPorUserId(Long userId);
    Adopcion buscarPorShelterId(Long shelterId);
    Adopcion obtenerAdopcionPorId(Long adoptionId);
    Adopcion actualizar(Adopcion adopcion);
    void eliminarAdopcion(Long adoptionId);
}
