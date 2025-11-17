package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.exception.*;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.enums.EstadoMascota;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import com.petconnect.pets.infraestructure.driver_adapters.jpa_repository.mascotas.ActualizationData;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor

public class MascotaUseCase {
    private final MascotaGateway mascotaGateway;

    public Mascota guardarMascota (Mascota mascota){
    if (mascota.getName()==null || mascota.getSpecies()==null|| mascota.getRace()==null||
            mascota.getAge()==null||mascota.getSex()==null || mascota.getChildFriendly()==
            null || mascota.getRequiresAmpleSpace() == null|| mascota.getSterilization() ==
            null || mascota.getVaccines() == null || mascota.getDescription() == null ||
            mascota.getImageUrl() == null || mascota.getState() == null){

        throw new IllegalArgumentException("Por favor complete todos los campos");
    }
    return mascotaGateway.guardar(mascota);
    }

    public Mascota buscarPorId(Long pet_id){
        try {
            return mascotaGateway.buscarPorId(pet_id);
        } catch (Exception error){
            System.out.println(error.getMessage());
        }
        return new Mascota();
    }

    public List<Mascota> obtenerTodas(int page, int size){
        return mascotaGateway.obtenerTodas(page, size);
    }

    public Mascota actualizarMascota(Long pet_id, ActualizationData data) {

            Mascota mascota = mascotaGateway.buscarPorId(pet_id);
            Mascota mascotaOriginal = mascotaGateway.buscarPorId(pet_id);


            if (mascota == null) {
                throw new MascotaNoEncontradaException("Mascota con id " + pet_id + " no existe");
            }

            if (data.getSterilization() != null) {
            boolean nuevoEstado = data.getSterilization();
            if (mascota.getSterilization() != null && mascota.getSterilization() && !nuevoEstado) {
                throw new ActualizacionEsterilizacionInvalidaException(
                        "No se puede cambiar el estado de esterilización de true a false."
                );
            }
            mascota.setSterilization(nuevoEstado);
            }
            if (data.getState()!=null){
                EstadoMascota estadoActual = mascota.getState();
                EstadoMascota nuevoEstado = data.getState();
                boolean transicionValida = switch (estadoActual){
                    case DISPONIBLE -> nuevoEstado == EstadoMascota.EN_PROCESO || nuevoEstado == EstadoMascota.DISPONIBLE;
                    case EN_PROCESO -> nuevoEstado == EstadoMascota.ADOPTADA|| nuevoEstado == EstadoMascota.EN_PROCESO;
                    case ADOPTADA -> nuevoEstado == EstadoMascota.DISPONIBLE;
                };
                if (!transicionValida){
                    throw new ActualizacionEstadoInvalidoException(
                            "No se puede cambiar el estado de "+estadoActual+" a "+nuevoEstado
                    );
                }
                mascota.setState(nuevoEstado);
            }




            if (data.getAge() != null) mascota.setAge(data.getAge());
            if (data.getChildFriendly() != null) mascota.setChildFriendly(data.getChildFriendly());
            if (data.getRequiresAmpleSpace() != null) mascota.setRequiresAmpleSpace(data.getRequiresAmpleSpace());
            if (data.getSterilization() != null) mascota.setSterilization(data.getSterilization());
            if (data.getVaccines() != null) mascota.setVaccines(data.getVaccines());
            if (data.getDescription() != null) mascota.setDescription(data.getDescription());
            if (data.getImageUrl() != null) mascota.setImageUrl(data.getImageUrl());
            if (data.getState() != null) mascota.setState(data.getState());

            return mascotaGateway.actualizarMascota(mascota);

    }

    public void eliminarMascota(Long id_mascota){
        mascotaGateway.eliminar(id_mascota);
    }


}
