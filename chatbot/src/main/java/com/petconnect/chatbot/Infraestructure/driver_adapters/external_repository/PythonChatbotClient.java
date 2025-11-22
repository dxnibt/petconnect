package com.petconnect.chatbot.Infraestructure.driver_adapters.external_repository;

import com.petconnect.chatbot.domain.model.ChatMessage;
import com.petconnect.chatbot.domain.model.gateway.PythonChatbotGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PythonChatbotClient implements PythonChatbotGateway {

    private final RestTemplate restTemplate;

    @Override
    public String sendMessage(ChatMessage msg) {

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://chatbotsito.onrender.com/chat",
                Map.of("pregunta", msg.getMessage()),
                Map.class
        );

        return (String) response.getBody().get("respuesta");
    }
}
