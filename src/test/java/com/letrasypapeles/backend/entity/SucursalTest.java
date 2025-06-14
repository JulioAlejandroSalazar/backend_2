package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SucursalTest {

    @Test
    void testBuilderAndGetters() {
        Sucursal sucursal = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Central")
                .direccion("Av. Siempre Viva 123")
                .region("Metropolitana")
                .build();

        assertEquals(1L, sucursal.getId());
        assertEquals("Sucursal Central", sucursal.getNombre());
        assertEquals("Av. Siempre Viva 123", sucursal.getDireccion());
        assertEquals("Metropolitana", sucursal.getRegion());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(2L);
        sucursal.setNombre("Sucursal Sur");
        sucursal.setDireccion("Calle Falsa 456");
        sucursal.setRegion("Valparaiso");

        assertEquals(2L, sucursal.getId());
        assertEquals("Sucursal Sur", sucursal.getNombre());
        assertEquals("Calle Falsa 456", sucursal.getDireccion());
        assertEquals("Valparaiso", sucursal.getRegion());
    }
}
