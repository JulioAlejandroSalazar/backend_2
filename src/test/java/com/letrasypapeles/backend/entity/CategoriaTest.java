package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaTest {

    @Test
    void testCategoriaBuilderAndGetters() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nombre("Papelería")
                .build();

        assertEquals(1L, categoria.getId());
        assertEquals("Papelería", categoria.getNombre());
    }

    @Test
    void testCategoriaSetters() {
        Categoria categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombre("Tecnología");

        assertEquals(2L, categoria.getId());
        assertEquals("Tecnología", categoria.getNombre());
    }
}
