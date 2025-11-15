package com.petconnect.auth.infraestructure.mapper;

import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio.RefugioData;
import org.springframework.stereotype.Component;

@Component
public class RefugioMapper {

    public Refugio toRefugio(RefugioData refugioData){

        return new Refugio(
                refugioData.getNit(),
                refugioData.getWebsite(),
                refugioData.getCurrentPets(),
                refugioData.getSupportDocument(),
                refugioData.isAprobado(),
                refugioData.getShelterDescription()
        );
    }

    public RefugioData toData(Refugio refugio){

        return new RefugioData(
                refugio.getNit(),
                refugio.getWebsite(),
                refugio.getCurrentPets(),
                refugio.getSupportDocument(),
                refugio.isAprobado(),
                refugio.getShelterDescription()
        );
    }

}
