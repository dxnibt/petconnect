package com.petconnect.pets.domain.model;


import com.petconnect.pets.domain.model.enums.EspecieMascota;
import com.petconnect.pets.domain.model.enums.EstadoMascota;
import com.petconnect.pets.domain.model.enums.SexoMascota;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Mascota {
    private Long pet_id;
    private String name;
    private EspecieMascota species;
    private String otherspecies;
    private String race;
    private LocalDate birthDate;
    private String age;
    private SexoMascota sex;
    private Boolean childFriendly;
    private Boolean requiresAmpleSpace;
    private Boolean sterilization;
    private String vaccines;
    private String description;
    private String imageUrl;
    private EstadoMascota state = EstadoMascota.DISPONIBLE;
    private Long shelter_Id;



}
