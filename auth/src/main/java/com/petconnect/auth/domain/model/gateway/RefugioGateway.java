package com.petconnect.auth.domain.model.gateway;

import com.petconnect.auth.domain.model.Refugio;

import java.util.List;

public interface RefugioGateway {

    Refugio guardarRefugio(Refugio refugio);
    Refugio buscarPorId(Long id);
    void eliminarPorId(Long id);
    Refugio actualizarRefugio(Refugio refugio);
    List<Refugio> listarNoAprobados(boolean aprobado, int page, int size);
}
