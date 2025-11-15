package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.exception.*;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
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

    public Mascota actualizarMascota(Mascota mascota){
        if (mascota.getName()==null || mascota.getSpecies()==null|| mascota.getRace()==null||
                mascota.getAge()==null||mascota.getSex()==null || mascota.getChildFriendly()==
                null || mascota.getRequiresAmpleSpace() == null|| mascota.getSterilization() ==
                null || mascota.getVaccines() == null || mascota.getDescription() == null ||
                mascota.getImageUrl() == null || mascota.getState() == null){
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        Mascota mascotaOriginal = mascotaGateway.buscarPorId(mascota.getPet_id());
        if (mascotaOriginal==null){
            throw new MascotaNoEncontradaException("La mascota no existe");
        }

        if (!mascotaOriginal.getName().equals(mascota.getName())){
            throw new NombreNoPuedeModificarseException("No es posible modificar el nombre de la mascota");
        }
        if (!mascotaOriginal.getRace().equals(mascota.getRace())){
            throw new RazaNoPuedeModificarseException("No es posible modificar la raza de la mascota");
        }
        if (!mascotaOriginal.getSex().equals(mascota.getSex())){
            throw new SexoNoPuedeModificarseException("No es posible modificar el sexo de la mascota");
        }
        if (!mascotaOriginal.getSpecies().equals(mascota.getSpecies())){
            throw new EspecieNoPuedeModificarseException("No es posible modificar la especie de la mascota");
        }

        return mascotaGateway.actualizarMascota(mascota);
    }

    public void eliminarMascota(Long id_mascota){
        mascotaGateway.eliminar(id_mascota);
    }


}
