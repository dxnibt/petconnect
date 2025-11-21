package com.petconnect.pets.domain.model.gateway;

import com.petconnect.pets.domain.model.Adopcion;

import java.util.List;

public interface AdopcionGateway {
    Adopcion crear(Adopcion adopcion);
    Adopcion buscarPorUserId(Long userId);
    List<Adopcion> buscarPorShelterId(Long shelterId, int page, int size);
    Adopcion obtenerAdopcionPorId(Long adoptionId);
    Adopcion actualizar(Adopcion adopcion);
    void eliminarAdopcion(Long adoptionId);
}
