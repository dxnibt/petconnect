package com.petconnect.auth.infraestructure.entry_points.message_broker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoNotificacionDTO {
    private String tipo;
    private String email;
    private String phoneNumber;
    private String mensaje;
}
