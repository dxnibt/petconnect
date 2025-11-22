package com.ecommerce.notification.infraestructure.sqs;

import com.ecommerce.notification.infraestructure.ses.SesEmailSender;
import com.ecommerce.notification.infraestructure.sns.SnsSmsSender;
import com.ecommerce.notification.infraestructure.sqs.dto.EventoNotificacionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class SqsListener {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final SnsSmsSender smsSender;
    private final SesEmailSender emailSender;

    private final String QUEUE_URL = "https://sqs.us-east-1.amazonaws.com/563076671467/notificaciones";

    @PostConstruct
    public void escucharMensajes() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                try {
                    ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                            .queueUrl(QUEUE_URL)
                            .maxNumberOfMessages(3)
                            .waitTimeSeconds(5)
                            .build();

                    List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();

                    for (Message message : messages) {
                        try {
                            System.out.println("Mensaje recibido de SQS: " + message.body());

                            EventoNotificacionDTO evento = objectMapper.readValue(message.body(), EventoNotificacionDTO.class);

                            // Procesar notificación
                            smsSender.enviarSms(evento.getMensaje(), evento.getPhoneNumber());
                            emailSender.enviarEmail(evento.getEmail(), evento.getTipo(), evento.getMensaje());

                            // Eliminar de la cola SOLO si salió bien
                            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                                    .queueUrl(QUEUE_URL)
                                    .receiptHandle(message.receiptHandle())
                                    .build());

                            System.out.println("Mensaje eliminado de la cola: " + message.messageId());
                        } catch (Exception e) {
                            System.err.println("Error procesando mensaje: " + message.messageId());
                            e.printStackTrace();
                        }
                    }
                } catch (Exception generalError) {
                    generalError.printStackTrace();
                }
            }
        });
    }

}
