package com.petconnect.pets.domain.model.gateway;

import com.petconnect.pets.domain.model.Mascota;

import java.util.List;

public interface MascotaGateway {

    Mascota guardar(Mascota mascota);
    Mascota buscarPorId(Long pet_id);
    List<Mascota> obtenerTodas(int page, int size);
    Mascota actualizarMascota(Mascota mascota);
    void eliminar(Long pet_id);

}
