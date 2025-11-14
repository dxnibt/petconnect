package com.petconnect.pets.infraestructure.driver_adapters.jpa_repository;


import com.petconnect.pets.domain.model.enums.EspecieMascota;
import com.petconnect.pets.domain.model.enums.EstadoMascota;
import com.petconnect.pets.domain.model.enums.SexoMascota;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mascotas")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class MascotaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pet_id;
    private String name;
    @Enumerated(EnumType.STRING)
    private EspecieMascota species;
    private String otherspecies;
    private String race;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private SexoMascota sex;
    private Boolean childFriendly;
    private Boolean requiresAmpleSpace;
    private Boolean sterilization;
    private String vaccines;
    private String description;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private EstadoMascota state;


}
