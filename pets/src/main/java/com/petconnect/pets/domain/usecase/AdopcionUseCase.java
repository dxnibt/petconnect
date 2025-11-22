package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.exception.*;
import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.enums.EstadoAdopcion;
import com.petconnect.pets.domain.model.enums.EstadoMascota;
import com.petconnect.pets.domain.model.gateway.AdopcionGateway;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import com.petconnect.pets.domain.model.gateway.UsuarioGateway;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.dtos.AdopcionDetalladaResponse;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.dtos.UsuarioResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class AdopcionUseCase {

    private final AdopcionGateway gateway;
    private final MascotaGateway mascotaGateway;
    private final UsuarioGateway usuarioGateway;

    public Adopcion crear(Long userId, Long petId) {
        // Validar que el usuario existe
        if (!usuarioGateway.usuarioExiste(userId)) {
            throw new UsuarioNoEncontradoException("El usuario no existe");
        }

        // Validar que el usuario tiene rol de adoptante
        if (!usuarioGateway.tieneRol(userId, "ADOPTANTE")) {
            throw new UsuarioNoAdoptanteException("Solo los adoptantes pueden realizar adopciones");
        }

        Adopcion adopcionExistente = gateway.buscarPorUserIdYEstado(userId, EstadoAdopcion.EN_PROCESO);
        if (adopcionExistente != null) {
            throw new UsuarioProcesoAdopcionException("Ya tienes una solicitud de adopción en proceso");
        }

        // Validar mascota
        Mascota mascota = mascotaGateway.buscarPorId(petId);
        if (mascota == null) {
            throw new MascotaNoEncontradaException("La mascota no existe");
        }
        if (mascota.getState() != EstadoMascota.DISPONIBLE) {
            throw new MascotaNoDisponibleException("La mascota no está disponible para adopción");
        }

        // Cambiar estado de la mascota a EN_PROCESO
        mascota.setState(EstadoMascota.EN_PROCESO);
        mascotaGateway.actualizarMascota(mascota);

        Adopcion adopcion = new Adopcion();
        adopcion.setUserId(userId);
        adopcion.setPetId(petId);
        adopcion.setShelterId(mascota.getShelter_Id());

        // Completar solicitud
        adopcion.setRequestDate(LocalDate.now().toString());
        adopcion.setStatus(EstadoAdopcion.EN_PROCESO);

        return gateway.crear(adopcion);
    }

    public List<Adopcion> obtenerPorShelterId(Long shelterId, int page, int size) {
        // Validar que el usuario (refugio) existe
        if (!usuarioGateway.usuarioExiste(shelterId)) {
            throw new UsuarioNoEncontradoException("El refugio no existe");
        }

        // Validar que el usuario tiene rol de REFUGIO
        if (!usuarioGateway.tieneRol(shelterId, "REFUGIO")) {
            throw new UsuarioNoAutorizadoException("Solo los refugios pueden ver sus solicitudes");
        }

        return gateway.buscarPorShelterId(shelterId, page, size);
    }


    public Adopcion aceptarAdopcion(Long adopcionId, Long shelterId) {
        // Validar que la adopción existe
        Adopcion adopcion = gateway.obtenerAdopcionPorId(adopcionId);
        if (adopcion == null) {
            throw new AdopcionNoEncontradaException("La adopción no existe");
        }

        // Validar que el usuario es REFUGIO
        if (!usuarioGateway.tieneRol(shelterId, "REFUGIO")) {
            throw new UsuarioNoAutorizadoException("Solo los refugios pueden aceptar adopciones");
        }

        // Validar que el refugio es dueño de esta adopción
        if (!adopcion.getShelterId().equals(shelterId)) {
            throw new UsuarioNoAutorizadoException("No puedes aceptar adopciones de otros refugios");
        }

        // Validar que la adopción está en proceso
        if (adopcion.getStatus() != EstadoAdopcion.EN_PROCESO) {
            throw new IllegalStateException("Solo se pueden aceptar adopciones en proceso");
        }

        // Cambiar estado de la adopción
        adopcion.setStatus(EstadoAdopcion.ACEPTADA);
        adopcion.setResponseDate(LocalDate.now().toString());

        // Cambiar estado de la mascota a ADOPTADA
        Mascota mascota = mascotaGateway.buscarPorId(adopcion.getPetId());
        if (mascota != null) {
            mascota.setState(EstadoMascota.ADOPTADA);
            mascotaGateway.actualizarMascota(mascota);
        }

        return gateway.actualizar(adopcion);
    }

    public Adopcion rechazarAdopcion(Long adopcionId, Long shelterId) {
        // Validar que la adopción existe
        Adopcion adopcion = gateway.obtenerAdopcionPorId(adopcionId);
        if (adopcion == null) {
            throw new AdopcionNoEncontradaException("La adopción no existe");
        }

        // Validar que el usuario es REFUGIO
        if (!usuarioGateway.tieneRol(shelterId, "REFUGIO")) {
            throw new UsuarioNoAutorizadoException("Solo los refugios pueden rechazar adopciones");
        }

        // Validar que el refugio es dueño de esta adopción
        if (!adopcion.getShelterId().equals(shelterId)) {
            throw new UsuarioNoAutorizadoException("No puedes rechazar adopciones de otros refugios");
        }

        // Validar que la adopción está en proceso
        if (adopcion.getStatus() != EstadoAdopcion.EN_PROCESO) {
            throw new IllegalStateException("Solo se pueden rechazar adopciones en proceso");
        }

        // Cambiar estado de la adopción
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


    //
    public Adopcion obtenerPorId(Long id) {
        Adopcion adopcion = gateway.obtenerAdopcionPorId(id);
        if (adopcion == null) {
            throw new AdopcionNoEncontradaException("La adopción no existe");
        }
        return adopcion;
    }

    public void eliminarSolicitudUsuario(Long adopcionId, Long usuarioId) {
        // Validar que la adopción existe
        Adopcion adopcion = gateway.obtenerAdopcionPorId(adopcionId);
        if (adopcion == null) {
            throw new AdopcionNoEncontradaException("La adopción no existe");
        }

        // Validar que el usuario es el dueño de la solicitud
        if (!adopcion.getUserId().equals(usuarioId)) {
            throw new UsuarioNoAutorizadoException("No puedes eliminar solicitudes de otros usuarios");
        }

        // Validar que el usuario tiene rol de adoptante
        if (!usuarioGateway.tieneRol(usuarioId, "ADOPTANTE")) {
            throw new UsuarioNoAutorizadoException("Solo los adoptantes pueden eliminar sus solicitudes");
        }

        // Validar que la adopción está en proceso
        if (adopcion.getStatus() != EstadoAdopcion.EN_PROCESO) {
            throw new IllegalStateException("Solo se pueden eliminar solicitudes en proceso");
        }

        // Liberar la mascota para que esté disponible nuevamente
        Mascota mascota = mascotaGateway.buscarPorId(adopcion.getPetId());
        if (mascota != null) {
            mascota.setState(EstadoMascota.DISPONIBLE);
            mascotaGateway.actualizarMascota(mascota);
        }

        // Eliminar la solicitud
        gateway.eliminarAdopcion(adopcionId);
    }

    public Adopcion obtenerSolicitudUsuario(Long usuarioId) {
        // Validar que el usuario existe
        if (!usuarioGateway.usuarioExiste(usuarioId)) {
            throw new UsuarioNoEncontradoException("El usuario no existe");
        }

        // Validar que el usuario tiene rol de adoptante
        if (!usuarioGateway.tieneRol(usuarioId, "ADOPTANTE")) {
            throw new UsuarioNoAutorizadoException("Solo los adoptantes pueden ver sus solicitudes");
        }

        // Buscar la solicitud en proceso del usuario
        return gateway.buscarPorUserIdYEstado(usuarioId, EstadoAdopcion.EN_PROCESO);
    }

    public List<Adopcion> obtenerTodasSolicitudesUsuario(Long usuarioId, int page, int size) {
        // Validar que el usuario existe
        if (!usuarioGateway.usuarioExiste(usuarioId)) {
            throw new UsuarioNoEncontradoException("El usuario no existe");
        }

        // Validar que el usuario tiene rol de ADOPTANTE
        if (!usuarioGateway.tieneRol(usuarioId, "ADOPTANTE")) {
            throw new UsuarioNoAutorizadoException("Solo los adoptantes pueden ver sus solicitudes");
        }

        return gateway.buscarTodasPorUserId(usuarioId, page, size);

    }

    public AdopcionDetalladaResponse obtenerDetallesCompletosParaRefugio(Long adopcionId, Long shelterId) {
        // Validar que la adopción existe
        Adopcion adopcion = gateway.obtenerAdopcionPorId(adopcionId);
        if (adopcion == null) {
            throw new AdopcionNoEncontradaException("La adopción no existe");
        }

        // Validar que el usuario es REFUGIO
        if (!usuarioGateway.tieneRol(shelterId, "REFUGIO")) {
            throw new UsuarioNoAutorizadoException("Solo los refugios pueden ver detalles completos de adopciones");
        }

        // Validar que el refugio es dueño de esta adopción
        if (!adopcion.getShelterId().equals(shelterId)) {
            throw new UsuarioNoAutorizadoException("No puedes ver adopciones de otros refugios");
        }

        // Obtener informacion completa de la mascota
        Mascota mascota = mascotaGateway.buscarPorId(adopcion.getPetId());
        if (mascota == null) {
            throw new MascotaNoEncontradaException("La mascota de esta adopción no existe");
        }

        // Obtener informacion completa del adoptante
        UsuarioResponse usuarioResponse = usuarioGateway.obtenerUsuarioCompleto(adopcion.getUserId());
        if (usuarioResponse == null) {
            throw new UsuarioNoEncontradoException("El usuario adoptante no existe");
        }

        // Retornar todos los datos
        return crearRespuestaCompleta(adopcion, mascota, usuarioResponse);
    }

    private AdopcionDetalladaResponse crearRespuestaCompleta(Adopcion adopcion, Mascota mascota, UsuarioResponse usuario) {
        AdopcionDetalladaResponse response = new AdopcionDetalladaResponse();

        //info adopción
        response.setIdAdoption(adopcion.getIdAdoption());
        response.setRequestDate(adopcion.getRequestDate());
        response.setStatus(adopcion.getStatus());
        response.setResponseDate(adopcion.getResponseDate());

        // info usuario
        response.setUserId(usuario.getId());
        response.setUserName(usuario.getName());
        response.setUserEmail(usuario.getEmail());
        response.setUserPhoneNumber(usuario.getPhoneNumber());
        response.setUserCity(usuario.getCity());
        response.setUserAddress(usuario.getAddress());
        response.setUserProfilePicture(usuario.getProfilePicture());

        // info extra adoptante
        response.setAdoptanteDocument(usuario.getDocument());
        response.setAdoptanteGender(usuario.getGender());
        response.setAdoptanteOtherGender(usuario.getOtherGender());
        response.setAdoptanteBirthDate(usuario.getBirthDate());
        response.setAdoptanteMonthlySalary(usuario.getMonthlySalary());
        response.setAdoptanteHousingType(usuario.getHousingType());
        response.setAdoptanteHasYard(usuario.getHasYard());
        response.setAdoptantePetExperience(usuario.getPetExperience());
        response.setAdoptanteHasOtherPets(usuario.getHasOtherPets());
        response.setAdoptanteHasChildren(usuario.getHasChildren());
        response.setAdoptanteHoursAwayFromHome(usuario.getHoursAwayFromHome());
        response.setAdoptantePreferredAnimalType(usuario.getPreferredAnimalType());
        response.setAdoptanteOtherPreferredAnimalType(usuario.getOtherPreferredAnimalType());
        response.setAdoptantePreferredPetSize(usuario.getPreferredPetSize());
        response.setAdoptanteActivityLevel(usuario.getActivityLevel());
        response.setAdoptantePersonalDescription(usuario.getPersonalDescription());

        // inf mascota
        response.setPetId(mascota.getPet_id());
        response.setPetName(mascota.getName());
        response.setPetSpecies(mascota.getSpecies() != null ? mascota.getSpecies().toString() : null);
        response.setPetOtherSpecies(mascota.getOtherspecies());
        response.setPetRace(mascota.getRace());
        response.setPetBirthDate(mascota.getBirthDate() != null ? mascota.getBirthDate().toString() : null);
        response.setPetAge(mascota.getAge());
        response.setPetSex(mascota.getSex() != null ? mascota.getSex().toString() : null);
        response.setPetChildFriendly(mascota.getChildFriendly());
        response.setPetRequiresAmpleSpace(mascota.getRequiresAmpleSpace());
        response.setPetSterilization(mascota.getSterilization());
        response.setPetVaccines(mascota.getVaccines());
        response.setPetDescription(mascota.getDescription());
        response.setPetImageUrl(mascota.getImageUrl());
        response.setPetState(mascota.getState() != null ? mascota.getState().toString() : null);
        response.setPetShelterId(mascota.getShelter_Id());

        return response;
    }


}