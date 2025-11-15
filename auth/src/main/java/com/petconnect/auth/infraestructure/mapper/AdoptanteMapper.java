package com.petconnect.auth.infraestructure.mapper;

import com.petconnect.auth.domain.model.Adoptante;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante.AdoptanteData;
import org.springframework.stereotype.Component;

@Component
public class AdoptanteMapper {

    public Adoptante toAdoptante(AdoptanteData adoptanteData){

        return new Adoptante(
                adoptanteData.getDocument(),
                adoptanteData.getGender(),
                adoptanteData.getOtherGender(),
                adoptanteData.getBirthDate(),
                adoptanteData.getMonthlySalary(),
                adoptanteData.getHousingType(),
                adoptanteData.isHasYard(),
                adoptanteData.isPetExperience(),
                adoptanteData.isHasOtherPets(),
                adoptanteData.isHasChildren(),
                adoptanteData.getHoursAwayFromHome(),
                adoptanteData.isHasAnimalExperience(),
                adoptanteData.getPreferredAnimalType(),
                adoptanteData.getOtherPreferredAnimalType(),
                adoptanteData.getPreferredPetSize(),
                adoptanteData.getActivityLevel(),
                adoptanteData.getPersonalDescription()
        );
    }

    public AdoptanteData toData(Adoptante adoptante){

        return new AdoptanteData(
                adoptante.getDocument(),
                adoptante.getGender(),
                adoptante.getOtherGender(),
                adoptante.getBirthDate(),
                adoptante.getMonthlySalary(),
                adoptante.getHousingType(),
                adoptante.isHasYard(),
                adoptante.isPetExperience(),
                adoptante.isHasOtherPets(),
                adoptante.isHasChildren(),
                adoptante.getHoursAwayFromHome(),
                adoptante.isHasAnimalExperience(),
                adoptante.getPreferredAnimalType(),
                adoptante.getOtherPreferredAnimalType(),
                adoptante.getPreferredPetSize(),
                adoptante.getActivityLevel(),
                adoptante.getPersonalDescription()
        );
    }

}

