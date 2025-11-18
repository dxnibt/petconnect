package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.adopcion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdopcionDataJpaRepository extends JpaRepository<AdopcionData, Long> {
    AdopcionData findByUserId(Long userId);
    AdopcionData findByShelterId(Long shelterId);
}
