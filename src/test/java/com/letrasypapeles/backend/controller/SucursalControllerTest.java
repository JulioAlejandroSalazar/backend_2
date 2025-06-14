package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.service.SucursalService;
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
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SucursalService sucursalService;

    @Autowired
    private ObjectMapper objectMapper;

    private Sucursal sucursal1;
    private Sucursal sucursal2;

    @BeforeEach
    void setup() {
        sucursal1 = new Sucursal();
        sucursal1.setId(1L);
        sucursal1.setNombre("Sucursal Centro");

        sucursal2 = new Sucursal();
        sucursal2.setId(2L);
        sucursal2.setNombre("Sucursal Norte");
    }

    @Test
    void obtenerTodas_debeRetornarListaSucursales() throws Exception {
        Mockito.when(sucursalService.obtenerTodas()).thenReturn(Arrays.asList(sucursal1, sucursal2));

        mockMvc.perform(get("/api/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Sucursal Centro")))
                .andExpect(jsonPath("$[1].nombre", is("Sucursal Norte")));
    }

    @Test
    void obtenerPorId_existente_debeRetornarSucursal() throws Exception {
        Mockito.when(sucursalService.obtenerPorId(1L)).thenReturn(Optional.of(sucursal1));

        mockMvc.perform(get("/api/sucursales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Sucursal Centro")));
    }

    @Test
    void obtenerPorId_noExistente_debeRetornar404() throws Exception {
        Mockito.when(sucursalService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sucursales/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearSucursal_debeGuardarYRetornarSucursal() throws Exception {
        Mockito.when(sucursalService.guardar(any(Sucursal.class))).thenReturn(sucursal1);

        mockMvc.perform(post("/api/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursal1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Sucursal Centro")));
    }

    @Test
    void actualizarSucursal_existente_debeActualizarYRetornarSucursal() throws Exception {
        Sucursal actualizada = new Sucursal();
        actualizada.setId(1L);
        actualizada.setNombre("Sucursal Actualizada");

        Mockito.when(sucursalService.obtenerPorId(1L)).thenReturn(Optional.of(sucursal1));
        Mockito.when(sucursalService.guardar(any(Sucursal.class))).thenReturn(actualizada);

        mockMvc.perform(put("/api/sucursales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Sucursal Actualizada")));
    }

    @Test
    void actualizarSucursal_noExistente_debeRetornar404() throws Exception {
        Mockito.when(sucursalService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/sucursales/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursal1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarSucursal_existente_debeRetornarOk() throws Exception {
        Mockito.when(sucursalService.obtenerPorId(1L)).thenReturn(Optional.of(sucursal1));

        mockMvc.perform(delete("/api/sucursales/1"))
                .andExpect(status().isOk());

        Mockito.verify(sucursalService).eliminar(1L);
    }

    @Test
    void eliminarSucursal_noExistente_debeRetornar404() throws Exception {
        Mockito.when(sucursalService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/sucursales/99"))
                .andExpect(status().isNotFound());
    }
}
