package com.petconnect.pets.application.config;

import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.model.gateway.AdopcionGateway;
import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import com.petconnect.pets.domain.usecase.AdopcionUseCase;
import com.petconnect.pets.domain.usecase.MascotaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UseCaseConfig {

    @Bean
    public MascotaUseCase mascotaUseCase(MascotaGateway mascotaGateway){
        return new MascotaUseCase(mascotaGateway);
    }
    @Bean
    public AdopcionUseCase adopcionUseCase(AdopcionGateway adopcionGateway){
        return new AdopcionUseCase(adopcionGateway);
    }

}
