package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacionServiceTest {

    @InjectMocks
    private NotificacionService notificacionService;

    @Mock
    private NotificacionRepository notificacionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarNotificacion() {
        Notificacion noti = new Notificacion();
        noti.setMensaje("Pedido despachado");

        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(noti);

        Notificacion result = notificacionService.guardar(noti);

        assertNotNull(result);
        assertEquals("Pedido despachado", result.getMensaje());
        verify(notificacionRepository).save(noti);
    }

    @Test
    void testBuscarPorId() {
        Notificacion noti = new Notificacion();
        noti.setId(3L);

        when(notificacionRepository.findById(3L)).thenReturn(Optional.of(noti));

        Optional<Notificacion> result = notificacionService.obtenerPorId(3L);

        assertTrue(result.isPresent());
        assertEquals(3L, result.get().getId());
        verify(notificacionRepository).findById(3L);
    }

    @Test
    void testObtenerTodas() {
        Notificacion n1 = new Notificacion();
        Notificacion n2 = new Notificacion();

        when(notificacionRepository.findAll()).thenReturn(Arrays.asList(n1, n2));

        List<Notificacion> notificaciones = notificacionService.obtenerTodas();

        assertEquals(2, notificaciones.size());
        verify(notificacionRepository).findAll();
    }

    @Test
    void testEliminarNotificacion() {
        Long id = 6L;

        notificacionService.eliminar(id);

        verify(notificacionRepository).deleteById(id);
    }
}
