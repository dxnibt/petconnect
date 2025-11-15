package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio;

import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refugios")
@AllArgsConstructor
@NoArgsConstructor
@Data
@PrimaryKeyJoinColumn(name = "usuario_id")
public class RefugioData extends UsuarioData {

    @Pattern(regexp = "^[0-9]{8,12}$", message = "El NIT debe contener solo números y entre 8 y 12 dígitos")
    private String nit;

    @Pattern(regexp = "^(https?:\\/\\/)?([\\w-]+\\.)+[\\w-]{2,}(\\/.*)?$", message = "La URL proporcionada no es válida")
    private String website;

    @Min(value = 0, message = "El número de mascotas no puede ser negativo")
    private Integer currentPets;

    private String supportDocument;

    @Column(nullable = false)
    private boolean aprobado = false;

    @Size(min = 20, message = "La descripción es demasiado corta")
    private String shelterDescription;

}
