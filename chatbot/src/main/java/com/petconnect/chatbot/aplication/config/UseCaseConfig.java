package com.petconnect.chatbot.aplication.config;

import com.petconnect.chatbot.domain.model.gateway.PythonChatbotGateway;
import com.petconnect.chatbot.domain.model.gateway.UsuarioGateway;
import com.petconnect.chatbot.domain.usecase.ChatbotUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ChatbotUseCase chatbotUseCase(PythonChatbotGateway pythonChatbotGateway,
                                         UsuarioGateway usuarioGateway) {
        return new ChatbotUseCase(pythonChatbotGateway, usuarioGateway);
    }
}
