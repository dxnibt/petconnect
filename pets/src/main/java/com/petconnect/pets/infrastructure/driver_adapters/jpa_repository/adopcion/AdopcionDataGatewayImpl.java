package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.adopcion;

import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.model.enums.EstadoAdopcion;
import com.petconnect.pets.domain.model.gateway.AdopcionGateway;
import com.petconnect.pets.infrastructure.mapper.AdopcionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdopcionDataGatewayImpl implements AdopcionGateway {

    private final AdopcionMapper mapper;
    private final AdopcionDataJpaRepository repository;
    private int currentPage = 0;

    @Override
    public Adopcion crear(Adopcion adopcion) {
        AdopcionData data = mapper.toData(adopcion);
        AdopcionData saved = repository.save(data);
        return mapper.toAdopcion(saved);
    }

    @Override
    public List<Adopcion> buscarPorShelterId(Long shelterId, int page, int size) {
        if (page == -1) {
            page = currentPage;
            currentPage++;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<AdopcionData> adopcionData = repository.findByShelterId(shelterId, pageable);

        if (adopcionData.isEmpty()) {
            currentPage = 0;
            return List.of();
        }
        return adopcionData.stream()
                .map(mapper::toAdopcion)
                .toList();
    }

    @Override
    public Adopcion obtenerAdopcionPorId(Long adoptionId) {
        return repository.findById(adoptionId)
                .map(mapper::toAdopcion)
                .orElse(null);
    }

    @Override
    public Adopcion actualizar(Adopcion adopcion) {
        AdopcionData data = mapper.toData(adopcion);
        AdopcionData updated = repository.save(data);
        return mapper.toAdopcion(updated);
    }

    @Override
    public Adopcion buscarPorUserIdYEstado(Long userId, EstadoAdopcion estado) {
        AdopcionData data = repository.findByUserIdAndStatus(userId, estado);
        return (data != null) ? mapper.toAdopcion(data) : null;
    }

    @Override
    public List<Adopcion> buscarTodasPorUserId(Long userId, int page, int size) {
        if (page == -1) {
            page = currentPage;
            currentPage++;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<AdopcionData> adopcionData = repository.findByUserId(userId, pageable);

        if (adopcionData.isEmpty()) {
            currentPage = 0;
            return List.of();
        }

        return adopcionData.stream()
                .map(mapper::toAdopcion)
                .toList();
    }

    @Override
    public void eliminarAdopcion(Long adoptionId) {
        repository.deleteById(adoptionId);
    }
}
