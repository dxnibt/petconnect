package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.adopcion;

import com.petconnect.pets.domain.model.enums.EstadoAdopcion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "adopciones")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdopcionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdoption;
    private Long userId;
    private Long petId;
    private Long shelterId;
    private String requestDate;

    @Enumerated(EnumType.STRING)
    private EstadoAdopcion status;
    private String responseDate;

}