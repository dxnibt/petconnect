package com.petconnect.auth.domain.model;

import com.petconnect.auth.domain.model.enums.adoptante.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Adoptante extends Usuario {

    private String document;
    private Gender gender;
    private String otherGender;
    private LocalDate birthDate;
    private Double monthlySalary;
    private HousingType housingType;
    private boolean hasYard;
    private boolean petExperience;
    private boolean hasOtherPets;
    private boolean hasChildren;
    private Integer hoursAwayFromHome;
    private PreferredAnimalType preferredAnimalType;
    private String otherPreferredAnimalType;
    private PreferredPetSize preferredPetSize;
    private ActivityLevel activityLevel;
    private String personalDescription;


}
