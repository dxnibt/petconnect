package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.exception.*;
import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.enums.EstadoAdopcion;
import com.petconnect.pets.domain.model.enums.EstadoMascota;
import com.petconnect.pets.domain.model.gateway.AdopcionGateway;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import com.petconnect.pets.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class AdopcionUseCase {

    private final AdopcionGateway gateway;
    private final MascotaGateway mascotaGateway;
    private final UsuarioGateway usuarioGateway;

    public Adopcion crear(Adopcion adopcion) {
        // Validar que el usuario existe
        if (!usuarioGateway.usuarioExiste(adopcion.getUserId())) {
            throw new UsuarioNoEncontradoException("El usuario no existe");
        }

        // Validar que el usuario tiene rol de adoptante
        if (!usuarioGateway.tieneRol(adopcion.getUserId(), "ADOPTANTE")) {
            throw new UsuarioNoAdoptanteException("Solo los adoptantes pueden realizar adopciones");
        }

        // Validar mascota
        Mascota mascota = mascotaGateway.buscarPorId(adopcion.getPetId());
        if (mascota == null) {
            throw new MascotaNoEncontradaException("La mascota no existe");
        }
        if (mascota.getState() != EstadoMascota.DISPONIBLE) {
            throw new MascotaNoDisponibleException("La mascota no está disponible para adopción");
        }

        // Cambiar estado de la mascota a EN_PROCESO
        mascota.setState(EstadoMascota.EN_PROCESO);
        mascotaGateway.actualizarMascota(mascota);

        // Completar solicitud
        adopcion.setRequestDate(LocalDate.now().toString());
        adopcion.setStatus(EstadoAdopcion.EN_PROCESO);

        return gateway.crear(adopcion);
    }

    public Adopcion obtenerPorId(Long id) {
        Adopcion adopcion = gateway.obtenerAdopcionPorId(id);
        if (adopcion == null) {
            throw new AdopcionNoEncontradaException("La adopción no existe");
        }
        return adopcion;
    }

    public Adopcion obtenerPorUserId(Long userId) {
        Adopcion adopcion = gateway.buscarPorUserId(userId);
        if (adopcion == null) {
            throw new AdopcionNoEncontradaException("El usuario no tiene solicitudes de adopción");
        }
        return adopcion;
    }

    public void eliminar(Long id) {
        Adopcion adopcion = obtenerPorId(id);

        // Si la adopción está en proceso, liberar la mascota
        if (adopcion.getStatus() == EstadoAdopcion.EN_PROCESO) {
            Mascota mascota = mascotaGateway.buscarPorId(adopcion.getPetId());
            if (mascota != null) {
                mascota.setState(EstadoMascota.DISPONIBLE);
                mascotaGateway.actualizarMascota(mascota);
            }
        }

        gateway.eliminarAdopcion(id);
    }

    public Adopcion aceptar(Long aId) {
        Adopcion adopcion = obtenerPorId(aId);

        if (adopcion.getStatus() != EstadoAdopcion.EN_PROCESO) {
            throw new IllegalStateException("Solo se pueden aceptar adopciones en proceso");
        }

        // Cambiar estado
        adopcion.setStatus(EstadoAdopcion.ACEPTADA);
        adopcion.setResponseDate(LocalDate.now().toString());

        // Cambiar estado de la mascota a ADOPTADA
        Mascota mascota = mascotaGateway.buscarPorId(adopcion.getPetId());
        if (mascota != null) {
            mascota.setState(EstadoMascota.ADOPTADA);
            mascotaGateway.actualizarMascota(mascota);
        }

//        try {
//            refugioGateway.restarMascota(adopcion.getShelterId());
//        } catch (Exception e) {
//            throw new ErrorRefugioException("Error al actualizar refugio");
//        }

        return gateway.actualizar(adopcion);
    }

    public Adopcion rechazar(Long aId) {
        Adopcion adopcion = obtenerPorId(aId);

        if (adopcion.getStatus() != EstadoAdopcion.EN_PROCESO) {
            throw new IllegalStateException("Solo se pueden rechazar adopciones en proceso");
        }

        adopcion.setStatus(EstadoAdopcion.RECHAZADA);
        adopcion.setResponseDate(LocalDate.now().toString());

        // Liberar la mascota para que esté disponible nuevamente
        Mascota mascota = mascotaGateway.buscarPorId(adopcion.getPetId());
        if (mascota != null) {
            mascota.setState(EstadoMascota.DISPONIBLE);
            mascotaGateway.actualizarMascota(mascota);
        }

        return gateway.actualizar(adopcion);
    }
}