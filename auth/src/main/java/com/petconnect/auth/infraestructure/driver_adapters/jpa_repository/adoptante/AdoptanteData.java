package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante;

import com.petconnect.auth.domain.model.enums.adoptante.*;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioData;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "adoptantes")
@AllArgsConstructor
@NoArgsConstructor
@Data
@PrimaryKeyJoinColumn(name = "usuario_id")
public class AdoptanteData extends UsuarioData {

    @Pattern(regexp = "^[0-9]{6,10}$", message = "El documento solo puede contener números y tener de 6 a 10 dígitos")
    private String document;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String otherGender;

    @Past(message = "La fecha de nacimiento debe ser anterior a la actual")
    private LocalDate birthDate;

    @DecimalMin(value = "1423500", message = "Debe tener ingresos al menos del salario mínimo vigente")
    private Double monthlySalary;

    @Enumerated(EnumType.STRING)
    private HousingType housingType;

    @Column(nullable = false)
    private Boolean hasYard;

    @Column(nullable = false)
    private Boolean petExperience;

    @Column(nullable = false)
    private Boolean hasOtherPets;

    @Column(nullable = false)
    private Boolean hasChildren;

    @Min(value = 0, message = "Las horas no pueden ser negativas")
    @Max(value = 24, message = "Las horas no pueden ser mayores a 24")
    private Integer hoursAwayFromHome;

    @Enumerated(EnumType.STRING)
    private PreferredAnimalType preferredAnimalType;

    private String otherPreferredAnimalType;

    @Enumerated(EnumType.STRING)
    private PreferredPetSize preferredPetSize;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Size(min = 10, message = "La descripción es demasiado corta")
    private String personalDescription;

}
