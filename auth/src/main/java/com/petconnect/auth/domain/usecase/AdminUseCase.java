package com.petconnect.auth.domain.usecase;

import com.petconnect.auth.domain.exception.RefugioNoEncontradoException;
import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.domain.model.gateway.RefugioGateway;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor

public class AdminUseCase {

    private final RefugioGateway refugioGateway;

    public List<Refugio>listarRefugiosPendientes(int page, int size) {
        return refugioGateway.listarNoAprobados(false, page, size);
    }

    public Refugio aprobarRefugio(Long id) {
        Refugio refugio = refugioGateway.buscarPorId(id);
        if (refugio == null) {
            throw new RefugioNoEncontradoException("Refugio no encontrado");
        }
        refugio.setAprobado(true);
        return refugioGateway.actualizarRefugio(refugio);
    }
}
