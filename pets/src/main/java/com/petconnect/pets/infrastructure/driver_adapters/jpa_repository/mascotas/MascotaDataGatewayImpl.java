package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.mascotas;


import com.petconnect.pets.domain.exception.MascotaNoEncontradaException;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import com.petconnect.pets.infrastructure.mapper.MascotaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor


public class MascotaDataGatewayImpl implements MascotaGateway {

    private final MascotaMapper mascotaMapper;
    private final MascotaDataJpaRepository repository;
    private int currentPage = 0;

    @Override
    public Mascota guardar(Mascota mascota) {
        MascotaData mascotaData = mascotaMapper.toData(mascota);
        return mascotaMapper.toMascota(repository.save(mascotaData));
    }

    @Override
    public Mascota buscarPorId(Long pet_id) {
        return repository.findById(pet_id)
                .map(mascotaData -> mascotaMapper.toMascota(mascotaData))
                .orElseThrow(() -> new MascotaNoEncontradaException("Mascota Inexistente"));

    }

    @Override
    public List<Mascota> obtenerTodas(int page, int size) {
        if (page == -1){
            page = currentPage;
            currentPage++;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<MascotaData> mascotaData = repository.findAll(pageable);
        if (mascotaData.isEmpty()){
            currentPage = 0;
            return List.of();
        }
        return mascotaData.stream().map(mascotaMapper::toMascota).toList();
    }

    @Override
    public Mascota actualizarMascota(Mascota mascota) {
        MascotaData mascotaDataActualizar = mascotaMapper.toData(mascota);
        return mascotaMapper.toMascota(repository.save(mascotaDataActualizar));
    }

    @Override
    public void eliminar(Long id_mascota) {
    if (!repository.existsById(id_mascota)){
        throw new MascotaNoEncontradaException("La mascota no existe");
    }
    repository.deleteById(id_mascota);
    }
}
