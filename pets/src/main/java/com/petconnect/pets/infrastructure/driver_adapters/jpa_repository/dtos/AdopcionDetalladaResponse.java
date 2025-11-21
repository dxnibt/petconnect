package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.dtos;

import com.petconnect.pets.domain.model.enums.EstadoAdopcion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdopcionDetalladaResponse {
    //inf adopcion
    private Long idAdoption;
    private String requestDate;
    private EstadoAdopcion status;
    private String responseDate;

    // info de usuario
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhoneNumber;
    private String userCity;
    private String userAddress;
    private String userProfilePicture;

    // info ext adoptante
    private String adoptanteDocument;
    private String adoptanteGender;
    private String adoptanteOtherGender;
    private String adoptanteBirthDate;
    private Double adoptanteMonthlySalary;
    private String adoptanteHousingType;
    private Boolean adoptanteHasYard;
    private Boolean adoptantePetExperience;
    private Boolean adoptanteHasOtherPets;
    private Boolean adoptanteHasChildren;
    private Integer adoptanteHoursAwayFromHome;
    private String adoptantePreferredAnimalType;
    private String adoptanteOtherPreferredAnimalType;
    private String adoptantePreferredPetSize;
    private String adoptanteActivityLevel;
    private String adoptantePersonalDescription;

    // info de mascota
    private Long petId;
    private String petName;
    private String petSpecies;
    private String petOtherSpecies;
    private String petRace;
    private String petBirthDate;
    private String petAge;
    private String petSex;
    private Boolean petChildFriendly;
    private Boolean petRequiresAmpleSpace;
    private Boolean petSterilization;
    private String petVaccines;
    private String petDescription;
    private String petImageUrl;
    private String petState;
    private Long petShelterId;
}
