package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void obtenerTodos_debeRetornarListaDePedidos() {
        List<Pedido> pedidosMock = List.of(new Pedido(), new Pedido());
        when(pedidoRepository.findAll()).thenReturn(pedidosMock);

        List<Pedido> pedidos = pedidoService.obtenerTodos();

        assertNotNull(pedidos);
        assertEquals(2, pedidos.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_conIdExistente_debeRetornarPedido() {
        Pedido pedidoMock = new Pedido();
        pedidoMock.setId(1L);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));

        Optional<Pedido> resultado = pedidoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_conIdNoExistente_debeRetornarVacio() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Pedido> resultado = pedidoService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());
        verify(pedidoRepository, times(1)).findById(99L);
    }

    @Test
    void guardar_debeGuardarYRetornarPedido() {
        Pedido pedidoMock = new Pedido();
        when(pedidoRepository.save(pedidoMock)).thenReturn(pedidoMock);

        Pedido resultado = pedidoService.guardar(pedidoMock);

        assertNotNull(resultado);
        verify(pedidoRepository, times(1)).save(pedidoMock);
    }

    @Test
    void eliminar_debeEliminarPedidoPorId() {
        doNothing().when(pedidoRepository).deleteById(1L);

        pedidoService.eliminar(1L);

        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void obtenerPorClienteId_debeRetornarListaPedidosDeCliente() {
        Long clienteId = 5L;
        List<Pedido> pedidosMock = List.of(new Pedido(), new Pedido());
        when(pedidoRepository.findByClienteId(clienteId)).thenReturn(pedidosMock);

        List<Pedido> resultado = pedidoService.obtenerPorClienteId(clienteId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pedidoRepository, times(1)).findByClienteId(clienteId);
    }

    @Test
    void obtenerPorEstado_debeRetornarListaPedidosPorEstado() {
        String estado = "PENDIENTE";
        List<Pedido> pedidosMock = List.of(new Pedido(), new Pedido(), new Pedido());
        when(pedidoRepository.findByEstado(estado)).thenReturn(pedidosMock);

        List<Pedido> resultado = pedidoService.obtenerPorEstado(estado);

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        verify(pedidoRepository, times(1)).findByEstado(estado);
    }
}
