package com.petconnect.auth.application.config;

import com.petconnect.auth.domain.model.gateway.AdoptanteGateway;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.RefugioGateway;
import com.petconnect.auth.domain.model.gateway.UsuarioGateway;
import com.petconnect.auth.domain.usecase.AdoptanteUseCase;
import com.petconnect.auth.domain.usecase.RefugioUseCase;
import com.petconnect.auth.domain.usecase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway, EncrypterGateway encrypterGateway , RefugioGateway refugioGateway, AdoptanteGateway adoptanteGateway){
        return new UsuarioUseCase(usuarioGateway, encrypterGateway, refugioGateway, adoptanteGateway);
    }

    @Bean
    public AdoptanteUseCase adoptanteUseCase(AdoptanteGateway adoptanteGateway, EncrypterGateway encrypterGateway, UsuarioUseCase usuarioUseCase){
        return new AdoptanteUseCase(adoptanteGateway, encrypterGateway, usuarioUseCase);
    }

    @Bean
    public RefugioUseCase refugioUseCase(RefugioGateway refugioGateway, EncrypterGateway encrypterGateway, UsuarioUseCase usuarioUseCase){
        return new RefugioUseCase(refugioGateway, encrypterGateway, usuarioUseCase);
    }

}

