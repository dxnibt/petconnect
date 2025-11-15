package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdoptanteDataJpaRepository extends JpaRepository<AdoptanteData, Long> {

    Optional<AdoptanteData> findByEmail(String email);
    Optional<AdoptanteData> findByDocument(String document);


}
