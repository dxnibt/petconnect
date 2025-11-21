package com.petconnect.pets.domain.model.gateway;

import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.model.enums.EstadoAdopcion;

import java.util.List;

public interface AdopcionGateway {
    Adopcion crear(Adopcion adopcion);
    List<Adopcion> buscarPorShelterId(Long shelterId, int page, int size);
    Adopcion obtenerAdopcionPorId(Long adoptionId);
    Adopcion buscarPorUserIdYEstado(Long userId, EstadoAdopcion estado);
    List<Adopcion> buscarTodasPorUserId(Long userId, int page, int size);
    Adopcion actualizar(Adopcion adopcion);
    void eliminarAdopcion(Long adoptionId);
}
