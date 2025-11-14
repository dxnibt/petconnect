package com.petconnect.auth.domain.model;

import com.petconnect.auth.domain.model.enums.usuario.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class Usuario {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String city;
    private String address;
    private String profilePicture;
    private Role role;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLogin;

}
