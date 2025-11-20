package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.mascotas;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.petconnect.pets.domain.model.enums.EspecieMascota;
import com.petconnect.pets.domain.model.enums.EstadoMascota;
import com.petconnect.pets.domain.model.enums.SexoMascota;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "mascotas")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class MascotaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pet_id;
    private String name;


    @Enumerated(EnumType.STRING)
    private EspecieMascota species;

    private String otherspecies;
    private String race;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    private String age;

    @Enumerated(EnumType.STRING)
    private SexoMascota sex;

    private Boolean childFriendly;
    private Boolean requiresAmpleSpace;
    private Boolean sterilization;
    private String vaccines;
    private String description;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private EstadoMascota state = EstadoMascota.DISPONIBLE;

    private Long shelterId;

}
