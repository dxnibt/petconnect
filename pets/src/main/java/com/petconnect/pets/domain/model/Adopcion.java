package com.petconnect.pets.domain.model;

import com.petconnect.pets.domain.model.enums.EstadoAdopcion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Adopcion {
    private Long idAdoption;
    private Long userId;
    private Long petId;
    private Long shelterId;
    private String requestDate;
    private EstadoAdopcion status;
    private String responseDate;
}