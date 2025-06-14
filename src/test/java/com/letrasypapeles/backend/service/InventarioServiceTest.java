package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.repository.InventarioRepository;
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
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void obtenerTodos_debeRetornarListaInventario() {
        List<Inventario> inventariosMock = List.of(new Inventario(), new Inventario());
        when(inventarioRepository.findAll()).thenReturn(inventariosMock);

        List<Inventario> inventarios = inventarioService.obtenerTodos();

        assertNotNull(inventarios);
        assertEquals(2, inventarios.size());
        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_conIdExistente_debeRetornarInventario() {
        Inventario inventarioMock = new Inventario();
        inventarioMock.setId(1L);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventarioMock));

        Optional<Inventario> resultado = inventarioService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(inventarioRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_conIdNoExistente_debeRetornarVacio() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Inventario> resultado = inventarioService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());
        verify(inventarioRepository, times(1)).findById(99L);
    }

    @Test
    void guardar_debeGuardarYRetornarInventario() {
        Inventario inventarioMock = new Inventario();
        when(inventarioRepository.save(inventarioMock)).thenReturn(inventarioMock);

        Inventario resultado = inventarioService.guardar(inventarioMock);

        assertNotNull(resultado);
        verify(inventarioRepository, times(1)).save(inventarioMock);
    }

    @Test
    void eliminar_debeEliminarInventarioPorId() {
        doNothing().when(inventarioRepository).deleteById(1L);

        inventarioService.eliminar(1L);

        verify(inventarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void obtenerPorProductoId_debeRetornarListaInventariosPorProducto() {
        Long productoId = 10L;
        List<Inventario> inventariosMock = List.of(new Inventario(), new Inventario());
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(inventariosMock);

        List<Inventario> resultado = inventarioService.obtenerPorProductoId(productoId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(inventarioRepository, times(1)).findByProductoId(productoId);
    }

    @Test
    void obtenerPorSucursalId_debeRetornarListaInventariosPorSucursal() {
        Long sucursalId = 5L;
        List<Inventario> inventariosMock = List.of(new Inventario());
        when(inventarioRepository.findBySucursalId(sucursalId)).thenReturn(inventariosMock);

        List<Inventario> resultado = inventarioService.obtenerPorSucursalId(sucursalId);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inventarioRepository, times(1)).findBySucursalId(sucursalId);
    }

    @Test
    void obtenerInventarioBajoUmbral_debeRetornarListaInventariosConCantidadMenorAlUmbral() {
        Integer umbral = 10;
        List<Inventario> inventariosMock = List.of(new Inventario(), new Inventario(), new Inventario());
        when(inventarioRepository.findByCantidadLessThan(umbral)).thenReturn(inventariosMock);

        List<Inventario> resultado = inventarioService.obtenerInventarioBajoUmbral(umbral);

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        verify(inventarioRepository, times(1)).findByCantidadLessThan(umbral);
    }
}
