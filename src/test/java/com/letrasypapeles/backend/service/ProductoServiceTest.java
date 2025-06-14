package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @InjectMocks
    private ProductoService productoService;

    @Mock
    private ProductoRepository productoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarProducto() {
        Producto producto = new Producto();
        producto.setNombre("Cuaderno");

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.guardar(producto);

        assertNotNull(result);
        assertEquals("Cuaderno", result.getNombre());
        verify(productoRepository).save(producto);
    }

    @Test
    void testBuscarProductoPorId() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Lápiz");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> result = productoService.obtenerPorId(1L);

        assertTrue(result.isPresent());
        assertEquals("Lápiz", result.get().getNombre());
        verify(productoRepository).findById(1L);
    }

    @Test
    void testListarProductos() {
        Producto p1 = new Producto();
        Producto p2 = new Producto();
        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> lista = productoService.obtenerTodos();

        assertEquals(2, lista.size());
        verify(productoRepository).findAll();
    }

    @Test
    void testEliminarProducto() {
        Long id = 2L;
        productoService.eliminar(id);
        verify(productoRepository).deleteById(id);
    }
}
