package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Role;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoleServiceTest {

    private final RoleService roleService = new RoleService();

    @Test
    void obtenerTodos_debeRetornarTodosLosRoles() {
        List<Role> roles = roleService.obtenerTodos();

        assertNotNull(roles);
        assertEquals(Role.values().length, roles.size());
        assertTrue(roles.contains(Role.ADMIN));
        assertTrue(roles.contains(Role.CLIENTE));
    }

    @Test
    void obtenerPorNombre_nombreExistente_debeRetornarRole() {
        Optional<Role> rol = roleService.obtenerPorNombre("admin");

        assertTrue(rol.isPresent());
        assertEquals(Role.ADMIN, rol.get());
    }

    @Test
    void obtenerPorNombre_nombreNoExistente_debeRetornarVacio() {
        Optional<Role> rol = roleService.obtenerPorNombre("noexiste");

        assertTrue(rol.isEmpty());
    }
}
