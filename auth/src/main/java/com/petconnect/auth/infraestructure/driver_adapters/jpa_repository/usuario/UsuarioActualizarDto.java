package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioActualizarDto {

    @Email(message = "Correo inválido")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número")
    private String password;

    @Pattern(regexp = "^3\\d{9}$", message = "Número de celular no válido")
    private String phoneNumber;

    private String city;
    private String address;
    private String profilePicture;

}
