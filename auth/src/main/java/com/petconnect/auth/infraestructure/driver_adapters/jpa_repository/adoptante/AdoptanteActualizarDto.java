package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante;
import com.petconnect.auth.domain.model.enums.adoptante.ActivityLevel;
import com.petconnect.auth.domain.model.enums.adoptante.HousingType;
import com.petconnect.auth.domain.model.enums.adoptante.PreferredAnimalType;
import com.petconnect.auth.domain.model.enums.adoptante.PreferredPetSize;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioActualizarDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdoptanteActualizarDto {

    private Double monthlySalary;
    private HousingType housingType;
    private boolean hasYard;
    private boolean petExperience;
    private boolean hasOtherPets;
    private boolean hasChildren;
    private Integer hoursAwayFromHome;
    private PreferredAnimalType preferredAnimalType;
    private String otherPreferredAnimalType;
    private PreferredPetSize preferredPetSize;
    private ActivityLevel activityLevel;
    private String personalDescription;
    private UsuarioActualizarDto usuarioActualizarDto;

}
