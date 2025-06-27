package com.letrasypapeles.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class LDAPControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRecursoPublico() throws Exception {
        mockMvc.perform(get("/publico"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bienvenido al recurso público de Empresa Z"));
    }

    @Test
    void testRecursoProtegido() throws Exception {
        mockMvc.perform(get("/protegido"))
                .andExpect(status().isOk())
                .andExpect(content().string("Acceso autorizado al recurso protegido de Empresa Z"));
    }
}
