package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;
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
    void testObtenerTodas() {
        List<Notificacion> lista = Arrays.asList(new Notificacion(), new Notificacion());
        when(notificacionRepository.findAll()).thenReturn(lista);

        List<Notificacion> resultado = notificacionService.obtenerTodas();

        assertEquals(2, resultado.size());
        verify(notificacionRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_Encontrado() {
        Notificacion n = new Notificacion();
        n.setId(1L);
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(n));

        Optional<Notificacion> resultado = notificacionService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(notificacionRepository).findById(1L);
    }

    @Test
    void testGuardar() {
        Notificacion n = new Notificacion();
        n.setMensaje("Mensaje de prueba");
        when(notificacionRepository.save(n)).thenReturn(n);

        Notificacion resultado = notificacionService.guardar(n);

        assertEquals("Mensaje de prueba", resultado.getMensaje());
        verify(notificacionRepository).save(n);
    }

    @Test
    void testEliminar() {
        Long id = 1L;
        doNothing().when(notificacionRepository).deleteById(id);

        notificacionService.eliminar(id);

        verify(notificacionRepository).deleteById(id);
    }

    @Test
    void testObtenerPorClienteId() {
        Long clienteId = 42L;
        List<Notificacion> lista = Arrays.asList(new Notificacion(), new Notificacion());
        when(notificacionRepository.findByClienteId(clienteId)).thenReturn(lista);

        List<Notificacion> resultado = notificacionService.obtenerPorClienteId(clienteId);

        assertEquals(2, resultado.size());
        verify(notificacionRepository).findByClienteId(clienteId);
    }

    @Test
    void testObtenerPorFechaEntre() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now();
        List<Notificacion> lista = Arrays.asList(new Notificacion());
        when(notificacionRepository.findByFechaBetween(inicio, fin)).thenReturn(lista);

        List<Notificacion> resultado = notificacionService.obtenerPorFechaEntre(inicio, fin);

        assertEquals(1, resultado.size());
        verify(notificacionRepository).findByFechaBetween(inicio, fin);
    }
}
