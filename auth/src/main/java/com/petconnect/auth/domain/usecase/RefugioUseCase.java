package com.petconnect.auth.domain.usecase;

import com.petconnect.auth.domain.exception.CamposIncompletosException;
import com.petconnect.auth.domain.exception.RefugioNoEncontradoException;
import com.petconnect.auth.domain.model.Notificacion;
import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.domain.model.gateway.EncrypterGateway;
import com.petconnect.auth.domain.model.gateway.NotificationGateway;
import com.petconnect.auth.domain.model.gateway.RefugioGateway;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio.RefugioActualizarDto;
import lombok.RequiredArgsConstructor;

import javax.management.Notification;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RefugioUseCase {

    private final RefugioGateway refugioGateway;
    private final EncrypterGateway encrypterGateway;
    private final UsuarioUseCase usuarioUseCase;
    private final NotificationGateway notificationGateway;

    public Refugio guardarRefugio(Refugio refugio) {

        if (!usuarioUseCase.isValidUsuario(refugio) || !isValidRefugio(refugio)) {
            throw new CamposIncompletosException("Por favor, complete todos los campos");
        }

        refugio.setAprobado(false);

        String passwordEncrypt = encrypterGateway.encrypt(refugio.getPassword());
        refugio.setPassword(passwordEncrypt);
        refugio.setRegistrationDate(LocalDateTime.now());
        Notificacion mensajeNotificacion = Notificacion.builder()
                .tipo("Registro Usuario")
                .email(refugio.getEmail())
                .phoneNumber(refugio.getPhoneNumber())
                .mensaje("Refugio registrado con exito, espere confirmación")
                .build();

        notificationGateway.enviarMensaje(mensajeNotificacion);
        return refugioGateway.guardarRefugio(refugio);

    }

    public Refugio actualizarRefugio(Long id, RefugioActualizarDto dto) {
        Refugio refugio = refugioGateway.buscarPorId(id);
        if (refugio == null) {
            throw new RefugioNoEncontradoException("Refugio no encontrado");
        }
        actualizarRefugioDto(refugio, dto);
        if (dto.getUsuarioActualizarDto() != null) {
            usuarioUseCase.actualizarUsuarioDto(refugio, dto.getUsuarioActualizarDto());
        }

        return refugioGateway.actualizarRefugio(refugio);
    }

    private boolean isValidRefugio(Refugio refugio){
        return  refugio.getNit() != null &&
                refugio.getWebsite() != null &&
                refugio.getSupportDocument() != null &&
                refugio.getShelterDescription() != null;
    }

    private void actualizarRefugioDto(Refugio refugio, RefugioActualizarDto dto){

        if (dto.getWebsite() != null) refugio.setWebsite(dto.getWebsite());
        if (dto.getShelterDescription() != null) refugio.setShelterDescription(dto.getShelterDescription());
    }
}
