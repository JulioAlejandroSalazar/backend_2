package com.letrasypapeles.backend;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import com.letrasypapeles.backend.entity.Notificacion;

public class NotificacionTest {

    @Test
    void notificacionFechaDebeSerReciente() {
        Notificacion noti = Notificacion.builder().fecha(LocalDateTime.now()).build();
        assertTrue(noti.getFecha().isBefore(LocalDateTime.now().plusSeconds(2)));
    }
    
}
