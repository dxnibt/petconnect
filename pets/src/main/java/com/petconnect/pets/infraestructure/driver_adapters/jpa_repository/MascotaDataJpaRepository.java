package com.petconnect.pets.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MascotaDataJpaRepository extends JpaRepository <MascotaData, Long> {
    Optional<MascotaData> findByName(String name);
    Optional<MascotaData> findBySpecies(String species);
}
