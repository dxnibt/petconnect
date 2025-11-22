package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.exception.*;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.enums.EstadoMascota;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import com.petconnect.pets.domain.model.gateway.UsuarioGateway;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.JwtDto.JwtUserDetails;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.mascotas.ActualizationData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MascotaUseCase {
    private final MascotaGateway mascotaGateway;

    public Mascota guardarMascota(Mascota mascota, JwtUserDetails userDetails) {
        // Validar permiso
        validarAcceso(userDetails, "guardar");

        // Validaciones del dominio
        validarFechaDeNacimiento(mascota.getBirthDate());
        String edadCalculada = calcularEdad(mascota.getBirthDate());
        mascota.setAge(edadCalculada);

        if (mascota.getName() == null || mascota.getRace() == null || mascota.getBirthDate() == null ||
                mascota.getAge() == null || mascota.getSex() == null || mascota.getChildFriendly() == null ||
                mascota.getRequiresAmpleSpace() == null || mascota.getSterilization() == null ||
                mascota.getVaccines() == null || mascota.getDescription() == null ||
                mascota.getImageUrl() == null) {
            throw new IllegalArgumentException("Por favor complete todos los campos");
        }

        if (mascota.getState() != EstadoMascota.DISPONIBLE && mascota.getState() != null) {
            throw new EstadoInicialNoValidoException("El estado no puede ser diferente de Disponible");
        }

        // Asignar refugioId si el usuario es REFUGIO
        if (tieneRol(userDetails, "REFUGIO")) {
            mascota.setShelter_Id(userDetails.getId());
        }

        mascota.setState(EstadoMascota.DISPONIBLE);

        return mascotaGateway.guardar(mascota);
    }

    public Mascota buscarPorId(Long pet_id) {
        Mascota mascota = mascotaGateway.buscarPorId(pet_id);
        if (mascota == null) {
            throw new MascotaNoEncontradaException("Mascota con id " + pet_id + " no existe");
        }
        return mascota;
    }

    public List<Mascota> obtenerTodas(int page, int size) {
        return mascotaGateway.obtenerTodas(page, size);
    }

    public Mascota actualizarMascota(Long pet_id, ActualizationData data, JwtUserDetails userDetails) {
        validarAcceso(userDetails, "actualizar");

        Mascota mascota = mascotaGateway.buscarPorId(pet_id);
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

        // Recalcular/validar edad según la birthDate actual de la mascota
        validarFechaDeNacimiento(mascota.getBirthDate());
        String edadCalculada = calcularEdad(mascota.getBirthDate());
        mascota.setAge(edadCalculada);

        // Aplicar cambios permitidos por el DTO de actualización
        if (data.getAge() != null) mascota.setAge(data.getAge());
        if (data.getChildFriendly() != null) mascota.setChildFriendly(data.getChildFriendly());
        if (data.getRequiresAmpleSpace() != null) mascota.setRequiresAmpleSpace(data.getRequiresAmpleSpace());
        if (data.getSterilization() != null) mascota.setSterilization(data.getSterilization());
        if (data.getVaccines() != null) mascota.setVaccines(data.getVaccines());
        if (data.getDescription() != null) mascota.setDescription(data.getDescription());
        if (data.getImageUrl() != null) mascota.setImageUrl(data.getImageUrl());

        return mascotaGateway.actualizarMascota(mascota);
    }

    public void eliminarMascota(Long id_mascota, JwtUserDetails userDetails) {
        validarAcceso(userDetails, "eliminar");
        Mascota mascota = mascotaGateway.buscarPorId(id_mascota);
        if (mascota == null) {
            throw new MascotaNoEncontradaException("Mascota con id " + id_mascota + " no existe");
        }
        mascotaGateway.eliminar(id_mascota);
    }


    private void validarAcceso(JwtUserDetails userDetails, String operacion) {
        if (userDetails == null) {
            throw new UsuarioNoAutenticadoException("Usuario no autenticado");
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role != null && role.startsWith("ROLE_") ? role.substring(5) : role)
                .collect(Collectors.toList());

        // Si es ADMIN o REFUGIO, permiten CRUD completo
        if (roles.contains("ADMIN") || roles.contains("REFUGIO")) {
            return;
        }

        // Si es ADOPTANTE, no permite CRUD
        if (roles.contains("ADOPTANTE")) {
            switch (operacion) {
                case "guardar":
                case "actualizar":
                case "eliminar":
                case "buscar":
                    throw new BloqueoDePermisosException("Los adoptantes solo pueden adoptar, no realizar CRUD de mascotas.");
                default:
                    throw new IllegalArgumentException("Operación desconocida: " + operacion);
            }
        }

        // Rol desconocido
        throw new IllegalArgumentException("Rol no permitido para esta operación");
    }

    private boolean tieneRol(JwtUserDetails userDetails, String rolBuscado) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r != null && r.startsWith("ROLE_") ? r.substring(5) : r)
                .anyMatch(rolBuscado::equalsIgnoreCase); // rolBuscado nunca es null
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

        if (birthDate.isAfter(today)) {
            throw new FechaDeNacimientoInvalidaException("La fecha de nacimiento no puede ser futura.");
        }

        int years = Period.between(birthDate, today).getYears();
        if (years > 30) {
            throw new EdadNoPermitidaException("La fecha de nacimiento ingresada no es válida.");
        }
    }
}

