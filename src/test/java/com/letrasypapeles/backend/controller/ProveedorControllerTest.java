package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.service.ProveedorService;
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
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProveedorService proveedorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Proveedor proveedor1;
    private Proveedor proveedor2;

    @BeforeEach
    void setUp() {
        proveedor1 = new Proveedor();
        proveedor1.setId(1L);
        proveedor1.setNombre("Proveedor A");

        proveedor2 = new Proveedor();
        proveedor2.setId(2L);
        proveedor2.setNombre("Proveedor B");
    }

    @Test
    void obtenerTodos_deberiaRetornarListaProveedores() throws Exception {
        Mockito.when(proveedorService.obtenerTodos()).thenReturn(Arrays.asList(proveedor1, proveedor2));

        mockMvc.perform(get("/api/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Proveedor A")))
                .andExpect(jsonPath("$[1].nombre", is("Proveedor B")));
    }

    @Test
    void obtenerPorId_existente_deberiaRetornarProveedor() throws Exception {
        Mockito.when(proveedorService.obtenerPorId(1L)).thenReturn(Optional.of(proveedor1));

        mockMvc.perform(get("/api/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Proveedor A")));
    }

    @Test
    void obtenerPorId_noExistente_deberiaRetornar404() throws Exception {
        Mockito.when(proveedorService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/proveedores/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearProveedor_deberiaRetornarProveedorCreado() throws Exception {
        Mockito.when(proveedorService.guardar(any(Proveedor.class))).thenReturn(proveedor1);

        mockMvc.perform(post("/api/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedor1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Proveedor A")));
    }

    @Test
    void actualizarProveedor_existente_deberiaRetornarProveedorActualizado() throws Exception {
        Mockito.when(proveedorService.obtenerPorId(1L)).thenReturn(Optional.of(proveedor1));
        Mockito.when(proveedorService.guardar(any(Proveedor.class))).thenReturn(proveedor2);

        mockMvc.perform(put("/api/proveedores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedor2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Proveedor B")));
    }

    @Test
    void actualizarProveedor_noExistente_deberiaRetornar404() throws Exception {
        Mockito.when(proveedorService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/proveedores/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedor1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarProveedor_existente_deberiaRetornarOk() throws Exception {
        Mockito.when(proveedorService.obtenerPorId(1L)).thenReturn(Optional.of(proveedor1));

        mockMvc.perform(delete("/api/proveedores/1"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarProveedor_noExistente_deberiaRetornar404() throws Exception {
        Mockito.when(proveedorService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/proveedores/99"))
                .andExpect(status().isNotFound());
    }
}
