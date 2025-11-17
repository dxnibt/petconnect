package com.petconnect.auth.domain.model.gateway;

import com.petconnect.auth.domain.model.Refugio;

public interface RefugioGateway {

    Refugio guardarRefugio(Refugio refugio);
    Refugio buscarPorId(Long id);
    void eliminarPorId(Long id);
    Refugio actualizarRefugio(Refugio refugio);
}
