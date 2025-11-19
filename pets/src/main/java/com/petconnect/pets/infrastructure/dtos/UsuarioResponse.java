package com.petconnect.pets.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioResponse {
    private Long id;
    private String role;
}