package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.UsuarioGateway;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio.RefugioDataJpaRepository;
import com.petconnect.auth.infraestructure.mapper.RefugioMapper;
import com.petconnect.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public String loginUsuario(Usuario usuario) {
        return null;
    }
}

