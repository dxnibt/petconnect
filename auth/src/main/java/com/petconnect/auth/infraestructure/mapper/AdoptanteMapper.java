package com.petconnect.auth.infraestructure.mapper;

import com.petconnect.auth.domain.model.Adoptante;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante.AdoptanteData;
import org.springframework.stereotype.Component;

@Component
public class AdoptanteMapper {

    public Adoptante toAdoptante(AdoptanteData adoptanteData) {
        if (adoptanteData == null) return null;

        Adoptante adoptante = new Adoptante(
                adoptanteData.getDocument(),
                adoptanteData.getGender(),
                adoptanteData.getOtherGender(),
                adoptanteData.getBirthDate(),
                adoptanteData.getMonthlySalary(),
                adoptanteData.getHousingType(),
                adoptanteData.getHasYard(),
                adoptanteData.getPetExperience(),
                adoptanteData.getHasOtherPets(),
                adoptanteData.getHasChildren(),
                adoptanteData.getHoursAwayFromHome(),
                adoptanteData.getPreferredAnimalType(),
                adoptanteData.getOtherPreferredAnimalType(),
                adoptanteData.getPreferredPetSize(),
                adoptanteData.getActivityLevel(),
                adoptanteData.getPersonalDescription()
        );

        adoptante.setId(adoptanteData.getId());
        adoptante.setName(adoptanteData.getName());
        adoptante.setEmail(adoptanteData.getEmail());
        adoptante.setPassword(adoptanteData.getPassword());
        adoptante.setPhoneNumber(adoptanteData.getPhoneNumber());
        adoptante.setCity(adoptanteData.getCity());
        adoptante.setAddress(adoptanteData.getAddress());
        adoptante.setProfilePicture(adoptanteData.getProfilePicture());
        adoptante.setRole(adoptanteData.getRole());

        return adoptante;
    }

    public AdoptanteData toData(Adoptante adoptante) {
        if (adoptante == null) return null;

        AdoptanteData adoptanteData = new AdoptanteData(
                adoptante.getDocument(),
                adoptante.getGender(),
                adoptante.getOtherGender(),
                adoptante.getBirthDate(),
                adoptante.getMonthlySalary(),
                adoptante.getHousingType(),
                adoptante.getHasYard(),
                adoptante.getPetExperience(),
                adoptante.getHasOtherPets(),
                adoptante.getHasChildren(),
                adoptante.getHoursAwayFromHome(),
                adoptante.getPreferredAnimalType(),
                adoptante.getOtherPreferredAnimalType(),
                adoptante.getPreferredPetSize(),
                adoptante.getActivityLevel(),
                adoptante.getPersonalDescription()
        );

        adoptanteData.setId(adoptante.getId());
        adoptanteData.setName(adoptante.getName());
        adoptanteData.setEmail(adoptante.getEmail());
        adoptanteData.setPassword(adoptante.getPassword());
        adoptanteData.setPhoneNumber(adoptante.getPhoneNumber());
        adoptanteData.setCity(adoptante.getCity());
        adoptanteData.setAddress(adoptante.getAddress());
        adoptanteData.setProfilePicture(adoptante.getProfilePicture());
        adoptanteData.setRole(adoptante.getRole());

        return adoptanteData;
    }
}
