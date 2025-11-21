package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.adopcion;

import com.petconnect.pets.domain.model.enums.EstadoAdopcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdopcionDataJpaRepository extends JpaRepository<AdopcionData, Long> {
    AdopcionData findByUserIdAndStatus(Long userId, EstadoAdopcion status);
    Page<AdopcionData> findByShelterId(Long shelterId, Pageable pageable);
    Page<AdopcionData> findByUserId(Long userId, Pageable pageable);
}
