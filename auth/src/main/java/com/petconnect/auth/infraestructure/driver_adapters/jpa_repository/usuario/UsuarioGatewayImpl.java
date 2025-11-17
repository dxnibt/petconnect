package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.model.gateway.UsuarioGateway;
import com.petconnect.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioDataJpaRepository repository;

    @Override
    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .map(usuarioData -> usuarioMapper.toUsuario(usuarioData))
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
    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .map(usuarioMapper::toUsuario)
                .orElse(null);
    }

    @Override
    public void actualizarLastLogin(Long id, LocalDateTime fecha) {
        UsuarioData usuarioData = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuarioData.setLastLogin(fecha);
        repository.save(usuarioData);
    }

}

