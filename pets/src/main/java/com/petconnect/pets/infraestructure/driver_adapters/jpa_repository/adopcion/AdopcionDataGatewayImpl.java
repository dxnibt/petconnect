package com.petconnect.pets.infraestructure.driver_adapters.jpa_repository.adopcion;

import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.model.gateway.AdopcionGateway;
import com.petconnect.pets.infraestructure.mapper.AdopcionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdopcionDataGatewayImpl implements AdopcionGateway {

    private final AdopcionMapper mapper;
    private final AdopcionDataJpaRepository repository;

    @Override
    public Adopcion crear(Adopcion adopcion) {
        AdopcionData data = mapper.toData(adopcion);
        AdopcionData saved = repository.save(data);
        return mapper.toAdopcion(saved);
    }

    @Override
    public Adopcion buscarPorUserId(Long userId) {
        AdopcionData data = repository.findByUserId(userId);
        return (data != null) ? mapper.toAdopcion(data) : null;
    }

    @Override
    public Adopcion buscarPorShelterId(Long shelterId) {
        AdopcionData data = repository.findByShelterId(shelterId);
        return (data != null) ? mapper.toAdopcion(data) : null;
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
    public void eliminarAdopcion(Long adoptionId) {
        repository.deleteById(adoptionId);
    }
}
