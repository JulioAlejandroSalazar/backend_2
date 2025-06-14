package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTodas_deberiaRetornarListaDeReservas() {
        Reserva r1 = new Reserva();
        Reserva r2 = new Reserva();
        when(reservaRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<Reserva> resultado = reservaService.obtenerTodas();

        assertEquals(2, resultado.size());
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarReserva() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Optional<Reserva> resultado = reservaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarOptionalVacio() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Reserva> resultado = reservaService.obtenerPorId(1L);

        assertFalse(resultado.isPresent());
        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    void guardar_deberiaGuardarReserva() {
        Reserva reserva = new Reserva();
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        Reserva resultado = reservaService.guardar(reserva);

        assertNotNull(resultado);
        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    void eliminar_deberiaEliminarReservaPorId() {
        doNothing().when(reservaRepository).deleteById(1L);

        reservaService.eliminar(1L);

        verify(reservaRepository, times(1)).deleteById(1L);
    }

    @Test
    void obtenerPorClienteId_deberiaRetornarListaDeReservas() {
        Reserva r1 = new Reserva();
        Reserva r2 = new Reserva();
        when(reservaRepository.findByClienteId(10L)).thenReturn(Arrays.asList(r1, r2));

        List<Reserva> resultado = reservaService.obtenerPorClienteId(10L);

        assertEquals(2, resultado.size());
        verify(reservaRepository, times(1)).findByClienteId(10L);
    }

    @Test
    void obtenerPorProductoId_deberiaRetornarListaDeReservas() {
        Reserva r1 = new Reserva();
        when(reservaRepository.findByProductoId(5L)).thenReturn(List.of(r1));

        List<Reserva> resultado = reservaService.obtenerPorProductoId(5L);

        assertEquals(1, resultado.size());
        verify(reservaRepository, times(1)).findByProductoId(5L);
    }

    @Test
    void obtenerPorEstado_deberiaRetornarListaDeReservas() {
        Reserva r1 = new Reserva();
        when(reservaRepository.findByEstado("CONFIRMADA")).thenReturn(List.of(r1));

        List<Reserva> resultado = reservaService.obtenerPorEstado("CONFIRMADA");

        assertEquals(1, resultado.size());
        verify(reservaRepository, times(1)).findByEstado("CONFIRMADA");
    }
}
