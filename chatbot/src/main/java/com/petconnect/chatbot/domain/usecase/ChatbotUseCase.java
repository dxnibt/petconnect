package com.petconnect.chatbot.domain.usecase;

import com.petconnect.chatbot.domain.exception.UsuarioNoEncontradoException;
import com.petconnect.chatbot.domain.model.ChatMessage;
import com.petconnect.chatbot.domain.model.gateway.PythonChatbotGateway;
import com.petconnect.chatbot.domain.model.gateway.UsuarioGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChatbotUseCase {
    private final PythonChatbotGateway gateway;
    private final UsuarioGateway usuarioGateway;

    public String sendMessage(ChatMessage message) {

        return gateway.sendMessage(message);
    }
}
