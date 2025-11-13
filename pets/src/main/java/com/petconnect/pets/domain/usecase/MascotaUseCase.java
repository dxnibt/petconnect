package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor

public class MascotaUseCase {
    private final MascotaGateway mascotaGateway;

    public Mascota guardarMascota (Mascota mascota){
    if (mascota == null ||
            Stream.of(
                    mascota.getNombre(),
                    mascota.getRaza(),
                    mascota.getEdad(),
                    mascota.getSexo(),
                    mascota.getRecomendadoParaNinos(),
                    mascota.getRequiereEspacioAmplio(),
                    mascota.getEsterilizacion(),
                    mascota.getVacunas(),
                    mascota.getDescripcion(),
                    mascota.getImagenUrl(),
                    mascota.getEstado()
            ).anyMatch(Objects::isNull)){
        throw new IllegalArgumentException("Por favor complete todos los campos");
    }
    return mascotaGateway.guardar(mascota);
    }

    public Mascota buscarPorId(Long id_mascota){
        try {
            return mascotaGateway.buscarPorId(id_mascota);
        } catch (Exception error) {
            System.out.println(error.getMessage());
            return null;
        }
    }

    public List<Mascota> obtenerTodas(int page, int size){
        return mascotaGateway.obtenerTodas(page, size);
    }

    public Mascota actualizarMascota(Mascota mascota){
        if (mascota == null ||
                Stream.of(
                        mascota.getNombre(),
                        mascota.getRaza(),
                        mascota.getEdad(),
                        mascota.getSexo(),
                        mascota.getRecomendadoParaNinos(),
                        mascota.getRequiereEspacioAmplio(),
                        mascota.getEsterilizacion(),
                        mascota.getVacunas(),
                        mascota.getDescripcion(),
                        mascota.getImagenUrl(),
                        mascota.getEstado()
                ).anyMatch(Objects::isNull)){
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        return mascotaGateway.actualizarMascota(mascota);
    }

    public void eliminarProducto(Long id_mascota){
        mascotaGateway.eliminar(id_mascota);
    }


}
