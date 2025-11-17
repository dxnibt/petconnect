package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante;
import com.petconnect.auth.domain.model.enums.adoptante.ActivityLevel;
import com.petconnect.auth.domain.model.enums.adoptante.HousingType;
import com.petconnect.auth.domain.model.enums.adoptante.PreferredAnimalType;
import com.petconnect.auth.domain.model.enums.adoptante.PreferredPetSize;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioActualizarDto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdoptanteActualizarDto {

    @DecimalMin(value = "1423500", message = "Debe tener ingresos al menos del salario mínimo vigente")
    private Double monthlySalary;

    private HousingType housingType;

    private Boolean hasYard;
    private Boolean petExperience;
    private Boolean hasOtherPets;
    private Boolean hasChildren;

    @Min(value = 0, message = "Las horas no pueden ser negativas")
    @Max(value = 24, message = "Las horas no pueden ser mayores a 24")
    private Integer hoursAwayFromHome;

    private PreferredAnimalType preferredAnimalType;

    private String otherPreferredAnimalType;

    private PreferredPetSize preferredPetSize;

    private ActivityLevel activityLevel;

    @Size(min = 10, message = "La descripción es demasiado corta")
    private String personalDescription;

    private UsuarioActualizarDto usuarioActualizarDto;

}
