package com.petconnect.auth.domain.usecase;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;


    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getPassword() == null || usuario.getEmail() == null) {
            throw new IllegalArgumentException("Por favor complete todos los campos");
        }

        if (!esMayorDeEdad(usuario.getFechaNacimiento())) {
            throw new IllegalArgumentException("El usuario debe ser mayor de edad");
        }

        String passwordEncrypt = encrypterGateway.encrypt(usuario.getPassword());
        usuario.setPassword(passwordEncrypt);
        return usuarioGateway.guardarUsuario(usuario);
    }


    public void eliminarUsuarioPorId(Long id){
        usuarioGateway.eliminarUsuario(id);
    }

    public Usuario buscarUsuarioPorId(Long id){
        try{
            return usuarioGateway.buscarPorId(id);
        } catch(Exception error){
            System.out.println(error.getMessage());
        }
        return new Usuario();
    }

    public Usuario actualizarUsuario(Usuario usuario){
        if(usuario.getId() == null || usuario.getPassword() == null){
            throw new IllegalArgumentException("El id y la contraseña son obligatorios");
        }
        if (!esMayorDeEdad(usuario.getFechaNacimiento())) {
            throw new IllegalArgumentException("El usuario debe ser mayor de edad");
        }

        String passwordEncrypt = encrypterGateway.encrypt(usuario.getPassword());
        usuario.setPassword(passwordEncrypt);
        return usuarioGateway.actualizarUsuario(usuario);
    }

    public String loginUsuario(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getPassword() == null) {
            throw new IllegalArgumentException("El email y la contraseña son obligatorios");
        }

        return usuarioGateway.loginUsuario(usuario);
    }

    public int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        return calcularEdad(fechaNacimiento) >= 18;
    }

}
