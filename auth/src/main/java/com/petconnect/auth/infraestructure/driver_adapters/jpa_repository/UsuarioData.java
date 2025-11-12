package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;

    @Column(length = 40, nullable = false)
    @Email
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$", message = "El correo debe cumplir con los parámetros")
    private String email;
    private String password;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String ciudad;
    private String direccion;
    private String fotoPerfil;
    private String role;

}
