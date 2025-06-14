package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reserva reserva1;
    private Reserva reserva2;

    @BeforeEach
    void setup() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Carlos");

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Lapiz");

        reserva1 = Reserva.builder()
                .id(1L)
                .fechaReserva(LocalDateTime.now())
                .estado("pendiente")
                .cliente(cliente)
                .producto(producto)
                .build();

        reserva2 = Reserva.builder()
                .id(2L)
                .fechaReserva(LocalDateTime.now().plusDays(1))
                .estado("confirmada")
                .cliente(cliente)
                .producto(producto)
                .build();
    }

    @Test
    void obtenerTodas_debeRetornarListaReservas() throws Exception {
        Mockito.when(reservaService.obtenerTodas()).thenReturn(Arrays.asList(reserva1, reserva2));

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].estado", is("pendiente")))
                .andExpect(jsonPath("$[1].estado", is("confirmada")));
    }

    @Test
    void obtenerPorId_existente_debeRetornarReserva() throws Exception {
        Mockito.when(reservaService.obtenerPorId(1L)).thenReturn(Optional.of(reserva1));

        mockMvc.perform(get("/api/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("pendiente")));
    }

    @Test
    void obtenerPorId_noExistente_debeRetornar404() throws Exception {
        Mockito.when(reservaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearReserva_debeRetornarReservaGuardada() throws Exception {
        Mockito.when(reservaService.guardar(any(Reserva.class))).thenReturn(reserva1);

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserva1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("pendiente")));
    }

    @Test
    void actualizarReserva_existente_debeRetornarReservaActualizada() throws Exception {
        Mockito.when(reservaService.obtenerPorId(1L)).thenReturn(Optional.of(reserva1));
        Mockito.when(reservaService.guardar(any(Reserva.class))).thenReturn(reserva2);

        mockMvc.perform(put("/api/reservas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserva2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("confirmada")));
    }

    @Test
    void actualizarReserva_noExistente_debeRetornar404() throws Exception {
        Mockito.when(reservaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/reservas/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserva1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarReserva_existente_debeRetornarOk() throws Exception {
        Mockito.when(reservaService.obtenerPorId(1L)).thenReturn(Optional.of(reserva1));

        mockMvc.perform(delete("/api/reservas/1"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarReserva_noExistente_debeRetornar404() throws Exception {
        Mockito.when(reservaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/reservas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorClienteId_debeRetornarListaReservas() throws Exception {
        Mockito.when(reservaService.obtenerPorClienteId(1L)).thenReturn(Arrays.asList(reserva1, reserva2));

        mockMvc.perform(get("/api/reservas/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
