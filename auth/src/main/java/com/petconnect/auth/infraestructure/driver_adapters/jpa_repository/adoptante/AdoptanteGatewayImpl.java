package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante;

import com.petconnect.auth.domain.model.Adoptante;
import com.petconnect.auth.domain.model.gateway.AdoptanteGateway;
import com.petconnect.auth.infraestructure.mapper.AdoptanteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdoptanteGatewayImpl implements AdoptanteGateway {

    private final AdoptanteMapper adoptanteMapper;
    private final AdoptanteDataJpaRepository repository;

    @Override
    public Adoptante guardarAdoptante(Adoptante adoptante) {

        Optional<AdoptanteData> emailExiste = repository.findByEmail(adoptante.getEmail());
        if(emailExiste.isPresent()){
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Optional<AdoptanteData> documentExiste = repository.findByDocument(adoptante.getDocument());
        if(documentExiste.isPresent()){
            throw new IllegalArgumentException("El documento ya está registrado");
        }

        AdoptanteData adoptanteData = adoptanteMapper.toData(adoptante);
        return adoptanteMapper.toAdoptante(repository.save(adoptanteData));

    }

    @Override
    public Adoptante buscarPorId(Long id) {
        return repository.findById(id)
                .map(adoptanteData -> adoptanteMapper.toAdoptante(adoptanteData))
                .orElseThrow(() -> new RuntimeException());
    }

    @Override
    public void eliminarPorId(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        repository.deleteById(id);
    }

    @Override
    public Adoptante actualizarAdoptante(Adoptante adoptante) {
        Optional<AdoptanteData> emailExiste = repository.findByEmail(adoptante.getEmail());
        if (emailExiste.isPresent() && !emailExiste.get().getId().equals(adoptante.getId())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        AdoptanteData adoptanteDataActualizar = adoptanteMapper.toData(adoptante);

        if (!repository.existsById(adoptanteDataActualizar.getId())) {
            throw new RuntimeException("Adoptante con id " + adoptanteDataActualizar.getId() + " no existe");
        }

        return adoptanteMapper.toAdoptante(repository.save(adoptanteDataActualizar));
    }

}
