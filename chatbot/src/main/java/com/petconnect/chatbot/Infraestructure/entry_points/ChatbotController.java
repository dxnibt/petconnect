package com.petconnect.chatbot.Infraestructure.entry_points;

import com.petconnect.chatbot.domain.exception.UsuarioNoEncontradoException;
import com.petconnect.chatbot.domain.model.ChatMessage;
import com.petconnect.chatbot.domain.usecase.ChatbotUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/petconnect/chatbot")
public class ChatbotController {

    private final ChatbotUseCase useCase;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessage message) {
        try {
            String respuesta = useCase.sendMessage(message);
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (UsuarioNoEncontradoException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
