package com.petconnect.auth.domain.usecase;

import com.petconnect.auth.domain.model.Adoptante;
import com.petconnect.auth.domain.model.gateway.AdoptanteGateway;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
public class AdoptanteUseCase {

    private final AdoptanteGateway adoptanteGateway;
    private final EncrypterGateway encrypterGateway;
    private final UsuarioUseCase usuarioUseCase;

    private static final double salario_minimo = 1423500;

    public Adoptante guardarAdoptante(Adoptante adoptante){

        if (!usuarioUseCase.isValidUsuario(adoptante) || !isValidAdoptante(adoptante)) {
            throw new IllegalArgumentException("Por favor, complete todos los campos");
        }

        if (!esMayorDeEdad(adoptante.getBirthDate())) {
            throw new IllegalArgumentException("El usuario debe ser mayor de edad.");
        }

        if (adoptante.getMonthlySalary() < salario_minimo) {
            throw new IllegalArgumentException("Debe contar con ingresos iguales o superiores al salario mínimo vigente en Colombia.");
        }

        String passwordEncrypt = encrypterGateway.encrypt(adoptante.getPassword());
        adoptante.setPassword(passwordEncrypt);
        return adoptanteGateway.guardarAdoptante(adoptante);

    }

    private boolean esMayorDeEdad(LocalDate birthDate) {
        return birthDate != null &&
                Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }

    private boolean isValidAdoptante(Adoptante adoptante) {
        return adoptante.getDocument() != null &&
                adoptante.getGender() != null &&
                adoptante.getBirthDate() != null &&
                adoptante.getMonthlySalary() != null &&
                adoptante.getHousingType() != null &&
                adoptante.getHoursAwayFromHome() != null &&
                adoptante.getPreferredAnimalType() != null &&
                adoptante.getPreferredPetSize() != null &&
                adoptante.getActivityLevel() != null &&
                adoptante.getPersonalDescription() != null &&
                !adoptante.getPersonalDescription().isBlank();
    }

}
