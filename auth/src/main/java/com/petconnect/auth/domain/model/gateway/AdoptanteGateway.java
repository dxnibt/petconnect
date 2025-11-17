package com.petconnect.auth.domain.model.gateway;

import com.petconnect.auth.domain.model.Adoptante;

public interface AdoptanteGateway {

    Adoptante guardarAdoptante(Adoptante adoptante);
    Adoptante buscarPorId(Long id);
    void eliminarPorId(Long id);
    Adoptante actualizarAdoptante(Adoptante adoptante);
}
