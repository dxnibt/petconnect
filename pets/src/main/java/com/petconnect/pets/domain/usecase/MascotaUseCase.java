package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.exception.*;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.enums.EstadoMascota;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.mascotas.ActualizationData;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;


@RequiredArgsConstructor

public class MascotaUseCase {
    private final MascotaGateway mascotaGateway;



    public Mascota guardarMascota (Mascota mascota){

        validarFechaDeNacimiento(mascota.getBirthDate());
        String edadCalculada = calcularEdad(mascota.getBirthDate());
        mascota.setAge(edadCalculada);

        if (mascota.getName()==null || mascota.getRace()==null|| mascota.getBirthDate()==null||
            mascota.getAge()==null||mascota.getSex()==null || mascota.getChildFriendly()==
            null || mascota.getRequiresAmpleSpace() == null|| mascota.getSterilization() ==
            null || mascota.getVaccines() == null || mascota.getDescription() == null ||
            mascota.getImageUrl() == null){

        throw new IllegalArgumentException("Por favor complete todos los campos");
    }
        if (mascota.getState()!=EstadoMascota.DISPONIBLE){
            throw new EstadoInicialNoValidoException("El estado no puede ser diferente de Disponible");
        }

        mascota.setState(EstadoMascota.DISPONIBLE);

        return mascotaGateway.guardar(mascota);
    }

    public Mascota buscarPorId(Long pet_id){
        try {
            return mascotaGateway.buscarPorId(pet_id);
        } catch (Exception error){
            System.out.println(error.getMessage());
        }
        return new Mascota();
    }

    public List<Mascota> obtenerTodas(int page, int size){
        return mascotaGateway.obtenerTodas(page, size);
    }

    public Mascota actualizarMascota(Long pet_id, ActualizationData data) {

            Mascota mascota = mascotaGateway.buscarPorId(pet_id);
            Mascota mascotaOriginal = mascotaGateway.buscarPorId(pet_id);


            if (mascota == null) {
                throw new MascotaNoEncontradaException("Mascota con id " + pet_id + " no existe");
            }

            if (data.getSterilization() != null) {
            boolean nuevoEstado = data.getSterilization();
            if (mascota.getSterilization() != null && mascota.getSterilization() && !nuevoEstado) {
                throw new ActualizacionEsterilizacionInvalidaException(
                        "No se puede cambiar el estado de esterilización de true a false."
                );
            }
            mascota.setSterilization(nuevoEstado);
            }

            validarFechaDeNacimiento(mascota.getBirthDate());
            String edadCalculada = calcularEdad(mascota.getBirthDate());
            mascota.setAge(edadCalculada);

            if (data.getAge() != null) mascota.setAge(data.getAge());
            if (data.getChildFriendly() != null) mascota.setChildFriendly(data.getChildFriendly());
            if (data.getRequiresAmpleSpace() != null) mascota.setRequiresAmpleSpace(data.getRequiresAmpleSpace());
            if (data.getSterilization() != null) mascota.setSterilization(data.getSterilization());
            if (data.getVaccines() != null) mascota.setVaccines(data.getVaccines());
            if (data.getDescription() != null) mascota.setDescription(data.getDescription());
            if (data.getImageUrl() != null) mascota.setImageUrl(data.getImageUrl());

            return mascotaGateway.actualizarMascota(mascota);

    }

    public void eliminarMascota(Long id_mascota){
        mascotaGateway.eliminar(id_mascota);
    }

    private String calcularEdad(LocalDate birthDate) {
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(birthDate, hoy);

        int years = periodo.getYears();
        int months = periodo.getMonths();
        int days = periodo.getDays();

        StringBuilder edad = new StringBuilder();

        if (years > 0) {
            edad.append(years).append(years == 1 ? " año" : " years");
            if (months > 0) edad.append(", ").append(months).append(months == 1 ? " mes" : " months");
            if (days > 0) edad.append(", ").append(days).append(days == 1 ? " día" : " días");
            return edad.toString();
        }

        if (months > 0) {
            edad.append(months).append(months == 1 ? " mes" : " months");
            if (days > 0) edad.append(", ").append(days).append(days == 1 ? " día" : " días");
            return edad.toString();
        }

        return days + (days == 1 ? " día" : " días");
    }


    private void validarFechaDeNacimiento(LocalDate birthDate) {

        if (birthDate == null) {
            throw new FechaDeNacimientoObligatoriaException("La fecha de nacimiento es obligatoria.");
        }

        LocalDate today = LocalDate.now();

        // 1. No puede ser en el futuro
        if (birthDate.isAfter(today)) {
            throw new FechaDeNacimientoInvalidaException("La fecha de nacimiento no puede ser futura.");
        }

        // 2. No puede ser demasiado antigua (mascota de más de 30 años)
        int years = Period.between(birthDate, today).getYears();
        if (years > 30) { // 30 años para perros/gatos es exagerado
            throw new EdadNoPermitidaException("La fecha de nacimiento ingresada no es válida.");
        }
    }



}
