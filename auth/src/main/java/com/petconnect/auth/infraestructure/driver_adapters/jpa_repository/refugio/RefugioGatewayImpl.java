package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio;

import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.domain.model.gateway.RefugioGateway;
import com.petconnect.auth.infraestructure.mapper.RefugioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefugioGatewayImpl implements RefugioGateway {

    private final RefugioMapper refugioMapper;
    private final RefugioDataJpaRepository repository;

    @Override
    public Refugio guardarRefugio(Refugio refugio) {

        Optional<RefugioData> emailExiste = repository.findByEmail(refugio.getEmail());
        if(emailExiste.isPresent()){
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Optional<RefugioData> nitExiste = repository.findByNit(refugio.getNit());
        if(nitExiste.isPresent()){
            throw new IllegalArgumentException("El nit ya está registrado");
        }

        RefugioData refugioData = refugioMapper.toData(refugio);
        return refugioMapper.toRefugio(repository.save(refugioData));
    }

    @Override
    public Refugio buscarPorId(Long id) {
        return repository.findById(id)
                .map(refugioData -> refugioMapper.toRefugio(refugioData))
                .orElseThrow(() -> new RuntimeException());
    }

    @Override
    public void eliminarPorId(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("El refugio no existe");
        }
        repository.deleteById(id);
    }

}
