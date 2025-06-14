package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.repository.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorServiceTest {

    @InjectMocks
    private ProveedorService proveedorService;

    @Mock
    private ProveedorRepository proveedorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarProveedor() {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Editorial Chile");

        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        Proveedor result = proveedorService.guardar(proveedor);

        assertNotNull(result);
        assertEquals("Editorial Chile", result.getNombre());
        verify(proveedorRepository).save(proveedor);
    }

    @Test
    void testBuscarPorId() {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(10L);
        proveedor.setNombre("Distribuidora ABC");

        when(proveedorRepository.findById(10L)).thenReturn(Optional.of(proveedor));

        Optional<Proveedor> result = proveedorService.obtenerPorId(10L);

        assertTrue(result.isPresent());
        assertEquals("Distribuidora ABC", result.get().getNombre());
        verify(proveedorRepository).findById(10L);
    }

    @Test
    void testObtenerTodos() {
        Proveedor p1 = new Proveedor();
        Proveedor p2 = new Proveedor();

        when(proveedorRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Proveedor> proveedores = proveedorService.obtenerTodos();

        assertEquals(2, proveedores.size());
        verify(proveedorRepository).findAll();
    }

    @Test
    void testEliminarProveedor() {
        Long id = 20L;

        proveedorService.eliminar(id);

        verify(proveedorRepository).deleteById(id);
    }
}
