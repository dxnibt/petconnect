package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioDataJpaRepository extends JpaRepository<UsuarioData,Long> {
    Optional<UsuarioData> findByEmail(String email);
}

