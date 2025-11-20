package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private Long userId;
    private String email;
    private String role;
    private String token;
    private String message;
}
