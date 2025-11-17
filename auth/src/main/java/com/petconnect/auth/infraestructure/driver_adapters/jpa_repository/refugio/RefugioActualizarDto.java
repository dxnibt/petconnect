package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio;

import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioActualizarDto;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefugioActualizarDto {

    @Pattern(regexp = "^(https?:\\/\\/)?([\\w\\-]+\\.)+[\\w\\-]+(\\/[^\\s]*)?$", message = "La URL proporcionada no es válida")
    private String website;

    @Size(min = 20, message = "La descripción es demasiado corta")
    private String shelterDescription;

    private UsuarioActualizarDto usuarioActualizarDto;

}
