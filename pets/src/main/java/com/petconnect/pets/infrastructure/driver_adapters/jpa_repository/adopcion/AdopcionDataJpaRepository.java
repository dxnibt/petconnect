package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.adopcion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdopcionDataJpaRepository extends JpaRepository<AdopcionData, Long> {
    AdopcionData findByUserId(Long userId);
    Page<AdopcionData> findByShelterId(Long shelterId, Pageable pageable);
}
