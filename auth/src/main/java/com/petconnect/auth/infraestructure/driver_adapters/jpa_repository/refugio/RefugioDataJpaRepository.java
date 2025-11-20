package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RefugioDataJpaRepository extends JpaRepository<RefugioData,Long> {

    Optional<RefugioData> findByEmail(String email);
    Optional<RefugioData> findByNit(String nit);
    Page<RefugioData> findByAprobado(boolean aprobado, Pageable pageable);

}
