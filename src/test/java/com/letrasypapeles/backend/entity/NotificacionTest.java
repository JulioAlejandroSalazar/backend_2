package com.letrasypapeles.backend.entity;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class NotificacionTest {

    @Test
    void notificacionFechaDebeSerReciente() {
        Notificacion noti = Notificacion.builder().fecha(LocalDateTime.now()).build();
        assertTrue(noti.getFecha().isBefore(LocalDateTime.now().plusSeconds(2)));
    }
    
}
