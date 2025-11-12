package com.petconnect.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private String password;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String ciudad;
    private String direccion;
    private String fotoPerfil;
    private String role;

}
