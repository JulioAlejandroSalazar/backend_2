package com.letrasypapeles.backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class LDAPController {

    @GetMapping("/publico")
    public String recursoPublico() {
        return "Bienvenido al recurso público de Empresa Z";
    }

    @GetMapping("/protegido")
    public String recursoProtegido() {
        return "Acceso autorizado al recurso protegido de Empresa Z";
    }
}