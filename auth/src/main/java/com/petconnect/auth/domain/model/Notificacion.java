package com.petconnect.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
@AllArgsConstructor

public class Notificacion {

    private String tipo;
    private String email;
    private String phoneNumber;
    private String mensaje;

}
