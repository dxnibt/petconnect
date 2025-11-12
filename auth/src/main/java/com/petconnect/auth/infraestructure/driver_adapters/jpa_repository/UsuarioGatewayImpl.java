package com.petconnect.auth.infraestructure.driver_adapters.jpa_repository;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.UsuarioGateway;
import com.petconnect.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioDataJpaRepository repository;
    private final EncrypterGateway encrypterGateway;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        Optional<UsuarioData> emailExiste = repository.findByEmail(usuario.getEmail());
        if (emailExiste.isPresent()){
            throw new IllegalArgumentException("El email ya está registrado");
        }

        UsuarioData usuarioData = usuarioMapper.toData(usuario);
        return usuarioMapper.toUsuario(repository.save(usuarioData));
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        repository.deleteById(id);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        // return usuarioMapper.toUsuario(repository.findById(id).get());
        return repository.findById(id)
                .map(usuarioData -> usuarioMapper.toUsuario(usuarioData))
                .orElseThrow(() -> new RuntimeException());
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        Optional<UsuarioData> emailExiste = repository.findByEmail(usuario.getEmail());
        if (emailExiste.isPresent() && !emailExiste.get().getId().equals(usuario.getId())){
            throw new IllegalArgumentException("El email ya está registrado");
        }
        UsuarioData usuarioDataActualizar = usuarioMapper.toData(usuario);
        if (!repository.existsById(usuarioDataActualizar.getId())) {
            throw new RuntimeException("Usuario con id " + usuarioDataActualizar.getId() + " no existe");
        }
        return usuarioMapper.toUsuario(repository.save(usuarioDataActualizar));
    }

    @Override
    public String loginUsuario(Usuario usuario) {
        UsuarioData usuarioData = repository.findByEmail(usuario.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!encrypterGateway.checkPassword(usuario.getPassword(), usuarioData.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return "Bienvenido";
    }


}

