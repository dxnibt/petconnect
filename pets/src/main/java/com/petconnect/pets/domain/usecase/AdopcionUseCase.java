package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.model.enums.EstadoAdopcion;
import com.petconnect.pets.domain.model.gateway.AdopcionGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class AdopcionUseCase {

    private final AdopcionGateway gateway;

    // Crear adopción (solo Adoptante)
    public Adopcion crear(Adopcion adopcion) {
        adopcion.setRequestDate(LocalDate.now().toString());
        adopcion.setStatus(EstadoAdopcion.EN_PROCESO);
        return gateway.crear(adopcion);
    }

    // Buscar por ID
    public Adopcion obtenerPorId(Long id) {
        return gateway.obtenerAdopcionPorId(id);
    }

    // Buscar por usuario
    public Adopcion obtenerPorUserId(Long userId) {
        return gateway.buscarPorUserId(userId);
    }

    // Eliminar
    public void eliminar(Long id) {
        gateway.eliminarAdopcion(id);
    }

    // Aceptar solicitud (solo refugio)
    public Adopcion aceptar(Long aId) {
        Adopcion adopcion = gateway.obtenerAdopcionPorId(aId);
        adopcion.setStatus(EstadoAdopcion.ACEPTADA);
        adopcion.setResponseDate(LocalDate.now().toString());
        return gateway.actualizar(adopcion);
    }

    // Rechazar solicitud (solo refugio)
    public Adopcion rechazar(Long aId) {
        Adopcion adopcion = gateway.obtenerAdopcionPorId(aId);
        adopcion.setStatus(EstadoAdopcion.RECHAZADA);
        adopcion.setResponseDate(LocalDate.now().toString());
        return gateway.actualizar(adopcion);
    }
}
