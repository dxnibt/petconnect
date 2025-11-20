package com.petconnect.auth.application.config;

import com.petconnect.auth.domain.model.gateway.*;
import com.petconnect.auth.domain.usecase.AdminUseCase;
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
    public AdoptanteUseCase adoptanteUseCase(AdoptanteGateway adoptanteGateway, EncrypterGateway encrypterGateway, UsuarioUseCase usuarioUseCase, NotificationGateway notificationGateway){
        return new AdoptanteUseCase(adoptanteGateway, encrypterGateway, usuarioUseCase, notificationGateway);
    }

    @Bean
    public RefugioUseCase refugioUseCase(RefugioGateway refugioGateway, EncrypterGateway encrypterGateway, UsuarioUseCase usuarioUseCase, NotificationGateway notificationGateway){
        return new RefugioUseCase(refugioGateway, encrypterGateway, usuarioUseCase, notificationGateway);
    }

    @Bean
    public AdminUseCase adminUseCase(RefugioGateway refugioGateway){
        return new AdminUseCase(refugioGateway);
    }
}

