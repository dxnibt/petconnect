package com.petconnect.pets.infraestructure.driver_adapters.jpa_repository.mascotas;

import com.petconnect.pets.domain.model.enums.EstadoMascota;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizationData {


        private Integer age;
        private Boolean childFriendly;
        private Boolean requiresAmpleSpace;
        private Boolean sterilization;
        private String vaccines;
        private String description;
        private String imageUrl;

        @Enumerated(EnumType.STRING)
        private EstadoMascota state;



}
