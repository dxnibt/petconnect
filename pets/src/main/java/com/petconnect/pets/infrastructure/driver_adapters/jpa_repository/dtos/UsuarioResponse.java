package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String city;
    private String address;
    private String profilePicture;
    private String role;

    // Campos de Adoptante
    private String document;
    private String gender;
    private String otherGender;
    private String birthDate;
    private Double monthlySalary;
    private String housingType;
    private Boolean hasYard;
    private Boolean petExperience;
    private Boolean hasOtherPets;
    private Boolean hasChildren;
    private Integer hoursAwayFromHome;
    private String preferredAnimalType;
    private String otherPreferredAnimalType;
    private String preferredPetSize;
    private String activityLevel;
    private String personalDescription;
}