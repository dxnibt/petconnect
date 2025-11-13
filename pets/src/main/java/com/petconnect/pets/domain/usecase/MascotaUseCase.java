package com.petconnect.pets.domain.usecase;

import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor

public class MascotaUseCase {
    private final MascotaGateway mascotaGateway;
}
