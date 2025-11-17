package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio;

import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioActualizarDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefugioActualizarDto {

    private String website;
    private String shelterDescription;
    private UsuarioActualizarDto usuarioActualizarDto;

}
