package com.petconnect.pets.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.petconnect.pets.domain.model.Adopcion;
import com.petconnect.pets.domain.model.gateway.*;
import com.petconnect.pets.domain.usecase.AdopcionUseCase;
import com.petconnect.pets.domain.usecase.MascotaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UseCaseConfig {

    @Bean
    public MascotaUseCase mascotaUseCase(MascotaGateway mascotaGateway, UsuarioGateway usuarioGateway){
        return new MascotaUseCase(mascotaGateway);
    }

    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public AdopcionUseCase adopcionUseCase(
            AdopcionGateway adopcionGateway,
            MascotaGateway mascotaGateway,
            UsuarioGateway usuarioGateway
    ){
        return new AdopcionUseCase(adopcionGateway, mascotaGateway,usuarioGateway);
    }

}
