package com.petconnect.pets.infraestructure.driver_adapters.jpa_repository;


import com.petconnect.pets.domain.exception.MascotaExisteException;
import com.petconnect.pets.domain.exception.MascotaNoEncontradaException;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import com.petconnect.pets.infraestructure.mapper.MascotaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new RuntimeException());

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
        if (!repository.existsById(mascotaDataActualizar.getPet_id())){
            throw new RuntimeException("Mascota con id "+mascotaDataActualizar.getPet_id()+ " no existe");
        }
        return mascotaMapper.toMascota(repository.save(mascotaDataActualizar));
    }

    @Override
    public void eliminar(Long id_mascota) {
    if (!repository.existsById(id_mascota)){
        throw new MascotaExisteException("La mascota no existe");
    }
    repository.deleteById(id_mascota);
    }
}
