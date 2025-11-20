package com.petconnect.auth.domain.usecase;

import com.petconnect.auth.domain.exception.*;
import com.petconnect.auth.domain.model.Adoptante;
import com.petconnect.auth.domain.model.Notificacion;
import com.petconnect.auth.domain.model.gateway.AdoptanteGateway;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.NotificationGateway;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante.AdoptanteActualizarDto;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@RequiredArgsConstructor
public class AdoptanteUseCase {

    private final AdoptanteGateway adoptanteGateway;
    private final EncrypterGateway encrypterGateway;
    private final UsuarioUseCase usuarioUseCase;
    private final NotificationGateway notificationGateway;

    private static final double salario_minimo = 1423500;

    public Adoptante guardarAdoptante(Adoptante adoptante){

        boolean usuarioValido = usuarioUseCase.isValidUsuario(adoptante);
        boolean adoptanteValido = isValidAdoptante(adoptante);

        if (!usuarioValido || !adoptanteValido) {
            throw new CamposIncompletosException("Por favor, complete todos los campos");
        }

        if (!esMayorDeEdad(adoptante.getBirthDate())) {
            throw new AdoptanteMenorEdadException("El usuario debe ser mayor de edad.");
        }

        if (adoptante.getMonthlySalary() < salario_minimo) {
            throw new SalarioNoAprobadoException("Debe contar con ingresos iguales o superiores al salario mínimo vigente en Colombia.");
        }

        if (adoptante.getBirthDate().isAfter(LocalDate.now())) {
            throw new FechaNacimientoFuturaException("La fecha de nacimiento no puede ser en el futuro");
        }

        if (adoptante.getMonthlySalary() <= 0) {
            throw new SalarioMenorIgualCeroException("El salario debe ser mayor a cero");
        }

        if (adoptante.getHoursAwayFromHome() < 0 || adoptante.getHoursAwayFromHome() > 24) {
            throw new HorasFueraCasaInvalidasException("Las horas fuera de casa deben estar entre 0 y 24");
        }

        String passwordEncrypt = encrypterGateway.encrypt(adoptante.getPassword());
        adoptante.setPassword(passwordEncrypt);

        adoptante.setRegistrationDate(LocalDateTime.now());

        Notificacion mensajeNotificacion = Notificacion.builder()
                .tipo("Registro Usuario")
                .email(adoptante.getEmail())
                .phoneNumber(adoptante.getPhoneNumber())
                .mensaje("Adoptante registrado con exito")
                .build();

        notificationGateway.enviarMensaje(mensajeNotificacion);

        return adoptanteGateway.guardarAdoptante(adoptante);
    }

    public Adoptante actualizarAdoptante(Long id, AdoptanteActualizarDto dto){
        Adoptante adoptante = adoptanteGateway.buscarPorId(id);
        if (adoptante == null) {
            throw new AdoptanteNoEncontradoException("Adoptante no encontrado");
        }
        actualizarAdoptanteDto(adoptante, dto);
        if (dto.getUsuarioActualizarDto() != null) {
            usuarioUseCase.actualizarUsuarioDto(adoptante, dto.getUsuarioActualizarDto());
        }
        return adoptanteGateway.actualizarAdoptante(adoptante);
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
                adoptante.getHasYard() != null &&
                adoptante.getPetExperience() != null &&
                adoptante.getHasOtherPets() != null &&
                adoptante.getHasChildren() != null &&
                adoptante.getHoursAwayFromHome() != null &&
                adoptante.getPreferredAnimalType() != null &&
                adoptante.getPreferredPetSize() != null &&
                adoptante.getActivityLevel() != null &&
                adoptante.getPersonalDescription() != null &&
                !adoptante.getPersonalDescription().isBlank();
    }

    private void actualizarAdoptanteDto(Adoptante adoptante, AdoptanteActualizarDto dto){
        if (dto.getMonthlySalary() != null) adoptante.setMonthlySalary(dto.getMonthlySalary());
        if (dto.getHousingType() != null) adoptante.setHousingType(dto.getHousingType());
        if (dto.getHasYard() != null) adoptante.setHasYard(dto.getHasYard());
        if (dto.getPetExperience() != null) adoptante.setPetExperience(dto.getPetExperience());
        if (dto.getHasOtherPets() != null) adoptante.setHasOtherPets(dto.getHasOtherPets());
        if (dto.getHasChildren() != null) adoptante.setHasChildren(dto.getHasChildren());
        if (dto.getHoursAwayFromHome() != null) adoptante.setHoursAwayFromHome(dto.getHoursAwayFromHome());
        if (dto.getPreferredAnimalType() != null) adoptante.setPreferredAnimalType(dto.getPreferredAnimalType());
        if (dto.getOtherPreferredAnimalType() != null) adoptante.setOtherPreferredAnimalType(dto.getOtherPreferredAnimalType());
        if (dto.getPreferredPetSize() != null) adoptante.setPreferredPetSize(dto.getPreferredPetSize());
        if (dto.getActivityLevel() != null) adoptante.setActivityLevel(dto.getActivityLevel());
        if (dto.getPersonalDescription() != null) adoptante.setPersonalDescription(dto.getPersonalDescription());
    }

}