package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @Email(message = "Correo inválido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

}
