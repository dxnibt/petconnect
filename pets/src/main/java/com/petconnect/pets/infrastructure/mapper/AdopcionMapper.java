package com.petconnect.pets.infrastructure.mapper;

import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.adopcion.AdopcionData;
import org.springframework.stereotype.Component;

@Component
public class AdopcionMapper {
    public Adopcion toAdopcion(AdopcionData adopcionData) {
        return new Adopcion(
                adopcionData.getIdAdoption(),
                adopcionData.getUserId(),
                adopcionData.getPetId(),
                adopcionData.getShelterId(),
                adopcionData.getRequestDate(),
                adopcionData.getStatus(),
                adopcionData.getResponseDate()
        );
    }
    public AdopcionData toData (Adopcion adopcion){
        return new AdopcionData(
                adopcion.getIdAdoption(),
                adopcion.getUserId(),
                adopcion.getPetId(),
                adopcion.getShelterId(),
                adopcion.getRequestDate(),
                adopcion.getStatus(),
                adopcion.getResponseDate()
        );
    }
}
