package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.exception.AdopcionNoEncontradaException;
import com.petconnect.pets.domain.exception.ErrorRefugioException;
import com.petconnect.pets.domain.exception.MascotaNoDisponibleException;
import com.petconnect.pets.domain.exception.MascotaNoEncontradaException;
import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.enums.EstadoAdopcion;
import com.petconnect.pets.domain.model.enums.EstadoMascota;
import com.petconnect.pets.domain.model.gateway.AdopcionGateway;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import com.petconnect.pets.domain.model.gateway.RefugioGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class AdopcionUseCase {

    private final AdopcionGateway gateway;
    private final MascotaGateway mascotaGateway;
    private final RefugioGateway refugioGateway;

    // Crear adopción (solo Adoptante)
    public Adopcion crear(Adopcion adopcion) {

        // Validar mascota
        Mascota mascota = mascotaGateway.buscarPorId(adopcion.getPetId());
        if (mascota == null) {
            throw new MascotaNoEncontradaException("La mascota no existe");
        }
        if (mascota.getState() != EstadoMascota.DISPONIBLE) {
            throw new MascotaNoDisponibleException("La mascota no está disponible");
        }


        // Completar solicitud
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
        if (adopcion == null) {
            throw new AdopcionNoEncontradaException("La adopción no existe");
        }
        // Cambiar estado
        adopcion.setStatus(EstadoAdopcion.ACEPTADA);
        adopcion.setResponseDate(LocalDate.now().toString());
        // Actualizar mascota
        Mascota mascota = mascotaGateway.buscarPorId(adopcion.getPetId());
        if (mascota != null) {
            mascota.setState(EstadoMascota.DISPONIBLE);
            mascotaGateway.actualizarMascota(mascota);
        }
        try {
            refugioGateway.restarMascota(adopcion.getShelterId());
        } catch (Exception e) {
            throw new ErrorRefugioException("Error al actualizar refugio");
        }
        return gateway.actualizar(adopcion);
    }

    // Rechazar solicitud (solo refugio)
    public Adopcion rechazar(Long aId) {
        Adopcion adopcion = gateway.obtenerAdopcionPorId(aId);
        if (adopcion == null) {
            throw new RuntimeException("La adopción no existe");
        }

        adopcion.setStatus(EstadoAdopcion.RECHAZADA);
        adopcion.setResponseDate(LocalDate.now().toString());

        return gateway.actualizar(adopcion);
    }
}
