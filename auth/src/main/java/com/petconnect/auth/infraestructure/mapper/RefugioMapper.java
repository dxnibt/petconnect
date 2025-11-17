package com.petconnect.auth.infraestructure.mapper;

import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio.RefugioData;
import org.springframework.stereotype.Component;

@Component
public class RefugioMapper {

    public Refugio toRefugio(RefugioData refugioData){
        if (refugioData == null) return null;
        Refugio refugio = new Refugio(
                refugioData.getNit(),
                refugioData.getWebsite(),
                refugioData.getCurrentPets(),
                refugioData.getSupportDocument(),
                refugioData.isAprobado(),
                refugioData.getShelterDescription()
        );

        refugio.setId(refugioData.getId());
        refugio.setName(refugioData.getName());
        refugio.setEmail(refugioData.getEmail());
        refugio.setPassword(refugioData.getPassword());
        refugio.setPhoneNumber(refugioData.getPhoneNumber());
        refugio.setCity(refugioData.getCity());
        refugio.setAddress(refugioData.getAddress());
        refugio.setProfilePicture(refugioData.getProfilePicture());
        refugio.setRole(refugioData.getRole());

        return refugio;
    }

    public RefugioData toData(Refugio refugio){
        if (refugio == null) return null;
        RefugioData refugioData = new RefugioData(
                refugio.getNit(),
                refugio.getWebsite(),
                refugio.getCurrentPets(),
                refugio.getSupportDocument(),
                refugio.isAprobado(),
                refugio.getShelterDescription()
        );
        refugioData.setId(refugio.getId());
        refugioData.setName(refugio.getName());
        refugioData.setEmail(refugio.getEmail());
        refugioData.setPassword(refugio.getPassword());
        refugioData.setPhoneNumber(refugio.getPhoneNumber());
        refugioData.setCity(refugio.getCity());
        refugioData.setAddress(refugio.getAddress());
        refugioData.setProfilePicture(refugio.getProfilePicture());
        refugioData.setRole(refugio.getRole());

        return refugioData;
    }

}
