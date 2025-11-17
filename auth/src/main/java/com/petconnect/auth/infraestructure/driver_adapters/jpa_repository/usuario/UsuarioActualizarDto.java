package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioActualizarDto {

    private String email;
    private String password;
    private String phoneNumber;
    private String city;
    private String address;
    private String profilePicture;

}
