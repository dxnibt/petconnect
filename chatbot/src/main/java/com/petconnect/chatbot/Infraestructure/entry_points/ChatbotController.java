package com.petconnect.chatbot.Infraestructure.entry_points;

import com.petconnect.chatbot.Infraestructure.driver_adapters.jpa_repository.JwtDto.JwtUserDetails;
import com.petconnect.chatbot.domain.model.ChatMessage;
import com.petconnect.chatbot.domain.model.gateway.PythonChatbotGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/petconnect/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final PythonChatbotGateway chatbotGateway;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMessage(
            @RequestBody ChatMessage chatMessage,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.badRequest().body(Map.of("respuesta", "Usuario no autenticado"));
        }

        String respuesta = chatbotGateway.sendMessage(chatMessage);
        return ResponseEntity.ok(Map.of("respuesta", respuesta));
    }
}