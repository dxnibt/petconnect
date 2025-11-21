package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.mascotas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class ActualizationData {


        private String age;
        private Boolean childFriendly;
        private Boolean requiresAmpleSpace;
        private Boolean sterilization;
        private String vaccines;
        private String description;
        private String imageUrl;

}
