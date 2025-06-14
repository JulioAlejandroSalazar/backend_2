package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.service.PedidoService;
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

    private Pedido crearPedidoMock() {
        // completa según los atributos reales
        return Pedido.builder()
                .id(1L)
                // .cliente(...)
                // .productos(...)
                // .fecha(...)
                // .estado("PENDIENTE")
                .build();
    }

    @Test
    void obtenerTodos_deberiaRetornarListaPedidos() throws Exception {
        Mockito.when(pedidoService.obtenerTodos()).thenReturn(Arrays.asList(crearPedidoMock()));

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_existente_deberiaRetornarPedido() throws Exception {
        Mockito.when(pedidoService.obtenerPorId(1L)).thenReturn(Optional.of(crearPedidoMock()));

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void crearPedido_deberiaRetornarPedidoCreado() throws Exception {
        Pedido pedido = crearPedidoMock();
        pedido.setId(null);

        Mockito.when(pedidoService.guardar(any(Pedido.class))).thenReturn(crearPedidoMock());

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarPedido_existente_deberiaRetornarPedidoActualizado() throws Exception {
        Mockito.when(pedidoService.obtenerPorId(1L)).thenReturn(Optional.of(crearPedidoMock()));
        Mockito.when(pedidoService.guardar(any(Pedido.class))).thenReturn(crearPedidoMock());

        mockMvc.perform(put("/api/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(crearPedidoMock())))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarPedido_existente_deberiaRetornarOk() throws Exception {
        Mockito.when(pedidoService.obtenerPorId(1L)).thenReturn(Optional.of(crearPedidoMock()));

        mockMvc.perform(delete("/api/pedidos/1"))
                .andExpect(status().isOk());
    }
}
