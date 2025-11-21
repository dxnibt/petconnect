package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioResponse {
    private Long id;
    private String role;
}