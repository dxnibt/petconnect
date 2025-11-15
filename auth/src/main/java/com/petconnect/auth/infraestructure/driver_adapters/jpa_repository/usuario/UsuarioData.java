package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario;

import com.petconnect.auth.domain.model.enums.usuario.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$", message = "El nombre solo puede tener letras")
    private String name;

    @Column(length = 50, nullable = false)
    @Email(message = "Correo inválido")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$", message = "El correo debe cumplir con los parámetros")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número")
    private String password;

    @Column(length = 10)
    @Pattern(regexp = "^3\\d{9}$", message = "Número de celular no válido")
    private String phoneNumber;

    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$", message = "La ciudad solo puede tener letras")
    private String city;

    @Size(min = 5, message = "La dirección es muy corta")
    private String address;

    @Column(length = 500)
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime registrationDate;
    private LocalDateTime lastLogin;

}
