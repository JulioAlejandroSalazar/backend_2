package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    public List<Role> obtenerTodos() {
        return Arrays.asList(Role.values());
    }

    public Optional<Role> obtenerPorNombre(String nombre) {
        return Arrays.stream(Role.values())
                     .filter(r -> r.name().equalsIgnoreCase(nombre))
                     .findFirst();
    }

}
