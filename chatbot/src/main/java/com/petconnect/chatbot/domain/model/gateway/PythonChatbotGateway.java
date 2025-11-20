package com.petconnect.chatbot.domain.model.gateway;

import com.petconnect.chatbot.domain.model.ChatMessage;

public interface PythonChatbotGateway {
    String sendMessage(ChatMessage msg);
}
