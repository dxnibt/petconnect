package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio;

import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.domain.model.gateway.RefugioGateway;
import com.petconnect.auth.infraestructure.mapper.RefugioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefugioGatewayImpl implements RefugioGateway {

    private final RefugioMapper refugioMapper;
    private final RefugioDataJpaRepository repository;
    private int currentPage = 0;

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
        refugio.setAprobado(false);

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

    @Override
    public Refugio actualizarRefugio(Refugio refugio) {
        Optional<RefugioData> emailExiste = repository.findByEmail(refugio.getEmail());
        if (emailExiste.isPresent() && !emailExiste.get().getId().equals(refugio.getId())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        RefugioData refugioDataActualizar = refugioMapper.toData(refugio);

        if (!repository.existsById(refugioDataActualizar.getId())) {
            throw new RuntimeException("Refugio con id " + refugioDataActualizar.getId() + " no existe");
        }


        return refugioMapper.toRefugio(repository.save(refugioDataActualizar));
    }

    @Override
    public List<Refugio> listarNoAprobados(boolean aprobado, int page, int size) {
        // Si no se pasa una página usamos el contador interno
        if (page == -1) {
            page = currentPage;
            currentPage++; // avanza una pagina por cada llamada
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<RefugioData> refugioData = repository.findByAprobado(aprobado, pageable);

        // Si no hay más refugios, reinicia contador y devuelve lista vacia
        if (refugioData.isEmpty()) {
            currentPage = 0;
            return List.of();
        }

        return refugioData.stream()
                .map(refugioMapper::toRefugio)
                .toList();
    }

}
