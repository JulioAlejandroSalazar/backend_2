package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Inventario inventario1;
    private Inventario inventario2;

    @BeforeEach
    void setup() {
        inventario1 = new Inventario();
        inventario1.setId(1L);
        inventario1.setCantidad(10);

        inventario2 = new Inventario();
        inventario2.setId(2L);
        inventario2.setCantidad(20);
    }

    @Test
    void obtenerTodos_debeRetornarListaInventarios() throws Exception {
        Mockito.when(inventarioService.obtenerTodos()).thenReturn(Arrays.asList(inventario1, inventario2));

        mockMvc.perform(get("/api/inventarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void obtenerPorId_existente_debeRetornarInventario() throws Exception {
        Mockito.when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.of(inventario1));

        mockMvc.perform(get("/api/inventarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void obtenerPorId_noExistente_debeRetornar404() throws Exception {
        Mockito.when(inventarioService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/inventarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorProductoId_debeRetornarLista() throws Exception {
        Mockito.when(inventarioService.obtenerPorProductoId(100L)).thenReturn(Arrays.asList(inventario1));

        mockMvc.perform(get("/api/inventarios/producto/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void obtenerPorSucursalId_debeRetornarLista() throws Exception {
        Mockito.when(inventarioService.obtenerPorSucursalId(200L)).thenReturn(Arrays.asList(inventario2));

        mockMvc.perform(get("/api/inventarios/sucursal/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void crearInventario_debeGuardarYRetornarInventario() throws Exception {
        Mockito.when(inventarioService.guardar(any(Inventario.class))).thenReturn(inventario1);

        mockMvc.perform(post("/api/inventarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cantidad", is(10)));
    }

    @Test
    void actualizarInventario_existente_debeActualizarYRetornarInventario() throws Exception {
        Inventario actualizado = new Inventario();
        actualizado.setId(1L);
        actualizado.setCantidad(99);

        Mockito.when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.of(inventario1));
        Mockito.when(inventarioService.guardar(any(Inventario.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/inventarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad", is(99)));
    }

    @Test
    void actualizarInventario_noExistente_debeRetornar404() throws Exception {
        Mockito.when(inventarioService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/inventarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarInventario_existente_debeRetornarOk() throws Exception {
        Mockito.when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.of(inventario1));

        mockMvc.perform(delete("/api/inventarios/1"))
                .andExpect(status().isOk());

        Mockito.verify(inventarioService).eliminar(1L);
    }

    @Test
    void eliminarInventario_noExistente_debeRetornar404() throws Exception {
        Mockito.when(inventarioService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/inventarios/99"))
                .andExpect(status().isNotFound());
    }
}
