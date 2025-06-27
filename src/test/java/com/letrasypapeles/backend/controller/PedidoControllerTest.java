package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.service.PedidoService;
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
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedido1;
    private Pedido pedido2;

    @BeforeEach
    void setup() {
        pedido1 = new Pedido();
        pedido1.setId(1L);
        pedido2 = new Pedido();
        pedido2.setId(2L);
    }

    @Test
    @WithMockUser
    void testObtenerTodos() throws Exception {
        Mockito.when(pedidoService.obtenerTodos()).thenReturn(Arrays.asList(pedido1, pedido2));

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void testObtenerPorId_Encontrado() throws Exception {
        Mockito.when(pedidoService.obtenerPorId(1L)).thenReturn(Optional.of(pedido1));

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testObtenerPorId_NoEncontrado() throws Exception {
        Mockito.when(pedidoService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pedidos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerPorClienteId() throws Exception {
        List<Pedido> pedidosCliente = Arrays.asList(pedido1);

        Mockito.when(pedidoService.obtenerPorClienteId(5L)).thenReturn(pedidosCliente);

        mockMvc.perform(get("/api/pedidos/cliente/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void testCrearPedido() throws Exception {
        Pedido nuevo = new Pedido();
        nuevo.setId(3L);

        Mockito.when(pedidoService.guardar(any(Pedido.class))).thenReturn(nuevo);

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)));
    }

    @Test
    void testActualizarPedido_Encontrado() throws Exception {
        Pedido actualizado = new Pedido();
        actualizado.setId(1L);

        Mockito.when(pedidoService.obtenerPorId(1L)).thenReturn(Optional.of(pedido1));
        Mockito.when(pedidoService.guardar(any(Pedido.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testActualizarPedido_NoEncontrado() throws Exception {
        Mockito.when(pedidoService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/pedidos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedido1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarPedido_Encontrado() throws Exception {
        Mockito.when(pedidoService.obtenerPorId(1L)).thenReturn(Optional.of(pedido1));

        mockMvc.perform(delete("/api/pedidos/1"))
                .andExpect(status().isOk());

        Mockito.verify(pedidoService).eliminar(1L);
    }

    @Test
    void testEliminarPedido_NoEncontrado() throws Exception {
        Mockito.when(pedidoService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/pedidos/99"))
                .andExpect(status().isNotFound());
    }
}
