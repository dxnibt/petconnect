package com.petconnect.auth.domain.model.gateway;

import com.petconnect.auth.domain.model.Notificacion;

public interface NotificationGateway {

    void enviarMensaje(Notificacion mensajeJson);
}
