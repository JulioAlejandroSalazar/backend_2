package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void testClienteBuilderAndGetters() {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nombre("Julio")
                .email("julio@example.com")
                .contraseña("secure123")
                .puntosFidelidad(150)
                .roles(Set.of(Role.CLIENTE, Role.USER))
                .build();

        assertEquals(1L, cliente.getId());
        assertEquals("Julio", cliente.getNombre());
        assertEquals("julio@example.com", cliente.getEmail());
        assertEquals("secure123", cliente.getContraseña());
        assertEquals(150, cliente.getPuntosFidelidad());
        assertTrue(cliente.getRoles().contains(Role.CLIENTE));
        assertTrue(cliente.getRoles().contains(Role.USER));
    }

    @Test
    void testClienteSetters() {
        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNombre("Ana");
        cliente.setEmail("ana@example.com");
        cliente.setContraseña("password456");
        cliente.setPuntosFidelidad(200);
        cliente.setRoles(Set.of(Role.ADMIN));

        assertEquals(2L, cliente.getId());
        assertEquals("Ana", cliente.getNombre());
        assertEquals("ana@example.com", cliente.getEmail());
        assertEquals("password456", cliente.getContraseña());
        assertEquals(200, cliente.getPuntosFidelidad());
        assertTrue(cliente.getRoles().contains(Role.ADMIN));
    }
}
