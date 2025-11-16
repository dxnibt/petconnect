package com.petconnect.pets.infraestructure.mapper;

import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.infraestructure.driver_adapters.jpa_repository.mascotas.MascotaData;
import org.springframework.stereotype.Component;

@Component
public class MascotaMapper {

    public Mascota toMascota(MascotaData mascotaData){

        return new Mascota(
                mascotaData.getPet_id(),
                mascotaData.getName(),
                mascotaData.getSpecies(),
                mascotaData.getOtherspecies(),
                mascotaData.getRace(),
                mascotaData.getAge(),
                mascotaData.getSex(),
                mascotaData.getChildFriendly(),
                mascotaData.getRequiresAmpleSpace(),
                mascotaData.getSterilization(),
                mascotaData.getVaccines(),
                mascotaData.getDescription(),
                mascotaData.getImageUrl(),
                mascotaData.getState()
        );
    }

    public MascotaData toData (Mascota mascota){
        return new MascotaData(
                mascota.getPet_id(),
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
        );
    }


}
