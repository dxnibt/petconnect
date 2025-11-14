package com.petconnect.pets.application.config;

import com.petconnect.pets.domain.model.gateway.MascotaGateway;
import com.petconnect.pets.domain.usecase.MascotaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UseCaseConfig {

    @Bean
    public MascotaUseCase mascotaUseCase(MascotaGateway mascotaGateway){
        return new MascotaUseCase(mascotaGateway);
    }

}
