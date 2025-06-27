package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Categoria;
import com.letrasypapeles.backend.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Categoria cat1;
    private Categoria cat2;

    @BeforeEach
    void setup() {
        cat1 = new Categoria();
        cat1.setId(1L);
        cat1.setNombre("Categoria 1");

        cat2 = new Categoria();
        cat2.setId(2L);
        cat2.setNombre("Categoria 2");
    }

    @Test
    @WithMockUser
    void testObtenerTodas() throws Exception {
        Mockito.when(categoriaService.obtenerTodas()).thenReturn(Arrays.asList(cat1, cat2));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Categoria 1")))
                .andExpect(jsonPath("$[1].nombre", is("Categoria 2")));
    }

    @Test
    void testObtenerPorId_Encontrado() throws Exception {
        Mockito.when(categoriaService.obtenerPorId(1L)).thenReturn(Optional.of(cat1));

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Categoria 1")));
    }

    @Test
    void testObtenerPorId_NoEncontrado() throws Exception {
        Mockito.when(categoriaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categorias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearCategoria() throws Exception {
        Categoria nueva = new Categoria();
        nueva.setId(3L);
        nueva.setNombre("Nueva Categoria");

        Mockito.when(categoriaService.guardar(any(Categoria.class))).thenReturn(nueva);

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nombre", is("Nueva Categoria")));
    }

    @Test
    void testActualizarCategoria_Encontrado() throws Exception {
        Categoria actualizada = new Categoria();
        actualizada.setId(1L);
        actualizada.setNombre("Categoria Actualizada");

        Mockito.when(categoriaService.obtenerPorId(1L)).thenReturn(Optional.of(cat1));
        Mockito.when(categoriaService.guardar(any(Categoria.class))).thenReturn(actualizada);

        mockMvc.perform(put("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Categoria Actualizada")));
    }

    @Test
    void testActualizarCategoria_NoEncontrado() throws Exception {
        Mockito.when(categoriaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/categorias/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cat1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarCategoria_Encontrado() throws Exception {
        Mockito.when(categoriaService.obtenerPorId(1L)).thenReturn(Optional.of(cat1));

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isOk());

        Mockito.verify(categoriaService).eliminar(1L);
    }

    @Test
    void testEliminarCategoria_NoEncontrado() throws Exception {
        Mockito.when(categoriaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/categorias/99"))
                .andExpect(status().isNotFound());
    }
}
