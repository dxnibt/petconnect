package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor

public class MascotaUseCase {
    private final MascotaGateway mascotaGateway;

    public Mascota guardarMascota (Mascota mascota){
    if (mascota == null ||
            Stream.of(
                    mascota.getName(),
                    mascota.getSpecies(),
                    mascota.getOtherspecies(),
                    mascota.getRace(),
                    mascota.getAge(),
                    mascota.getSex(),
                    mascota.getChildFriendly(),
                    mascota.getRequiresAmpleSpace(),
                    mascota.getSterilization(),
                    mascota.getVaccines(),
                    mascota.getDescription(),
                    mascota.getImageUrl(),
                    mascota.getState()
            ).anyMatch(Objects::isNull)){
        throw new IllegalArgumentException("Por favor complete todos los campos");
    }
    return mascotaGateway.guardar(mascota);
    }

    public Mascota buscarPorId(Long id_mascota){
        try {
            return mascotaGateway.buscarPorId(id_mascota);
        } catch (Exception error) {
            System.out.println(error.getMessage());
            return null;
        }
    }

    public List<Mascota> obtenerTodas(int page, int size){
        return mascotaGateway.obtenerTodas(page, size);
    }

    public Mascota actualizarMascota(Mascota mascota){
        if (mascota == null ||
                Stream.of(
                        mascota.getName(),
                        mascota.getSpecies(),
                        mascota.getOtherspecies(),
                        mascota.getRace(),
                        mascota.getAge(),
                        mascota.getSex(),
                        mascota.getChildFriendly(),
                        mascota.getRequiresAmpleSpace(),
                        mascota.getSterilization(),
                        mascota.getVaccines(),
                        mascota.getDescription(),
                        mascota.getImageUrl(),
                        mascota.getState()
                ).anyMatch(Objects::isNull)){
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        Mascota mascotaOriginal = mascotaGateway.buscarPorId(mascota.getPet_id());

        if (mascotaOriginal==null){
            throw new IllegalArgumentException("La mascota no existe");
        }

        if (!mascotaOriginal.getName().equals(mascota.getName())){
            throw new IllegalArgumentException("No es posible modificar el nombre de la mascota");
        }

        if (!mascotaOriginal.getRace().equals(mascota.getRace())){
            throw new IllegalArgumentException("No es posible modificar la raza de la mascota");
        }

        if (!mascotaOriginal.getSex().equals(mascota.getSex())){
            throw new IllegalArgumentException("No es posible modificar el sexo de la mascota");
        }
        if (!mascotaOriginal.getSpecies().equals(mascota.getSpecies())){
            throw new IllegalArgumentException("No es posible modificar la especie de la mascota");
        }

        return mascotaGateway.actualizarMascota(mascota);
    }

    public void eliminarProducto(Long id_mascota){
        mascotaGateway.eliminar(id_mascota);
    }


}
