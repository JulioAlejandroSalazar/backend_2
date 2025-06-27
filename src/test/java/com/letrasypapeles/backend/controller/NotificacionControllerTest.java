package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.service.NotificacionService;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;
    private Notificacion notificacion1;
    private Notificacion notificacion2;

    @BeforeEach
    void setup() {
        cliente = Cliente.builder()
                .id(10L)
                .nombre("Cliente Test")
                .build();

        notificacion1 = Notificacion.builder()
                .id(1L)
                .mensaje("Mensaje 1")
                .fecha(LocalDateTime.now().minusDays(1))
                .cliente(cliente)
                .build();

        notificacion2 = Notificacion.builder()
                .id(2L)
                .mensaje("Mensaje 2")
                .fecha(LocalDateTime.now())
                .cliente(cliente)
                .build();
    }

    @Test
    @WithMockUser
    void testObtenerTodas() throws Exception {
        Mockito.when(notificacionService.obtenerTodas()).thenReturn(Arrays.asList(notificacion1, notificacion2));

        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].cliente.id", is(10)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].cliente.nombre", is("Cliente Test")));
    }

    @Test
    void testObtenerPorId_Encontrado() throws Exception {
        Mockito.when(notificacionService.obtenerPorId(1L)).thenReturn(Optional.of(notificacion1));

        mockMvc.perform(get("/api/notificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.mensaje", is("Mensaje 1")))
                .andExpect(jsonPath("$.cliente.id", is(10)))
                .andExpect(jsonPath("$.cliente.nombre", is("Cliente Test")));
    }

    @Test
    void testObtenerPorId_NoEncontrado() throws Exception {
        Mockito.when(notificacionService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notificaciones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerPorClienteId() throws Exception {
        List<Notificacion> notisCliente = Arrays.asList(notificacion1, notificacion2);

        Mockito.when(notificacionService.obtenerPorClienteId(10L)).thenReturn(notisCliente);

        mockMvc.perform(get("/api/notificaciones/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].cliente.id", is(10)))
                .andExpect(jsonPath("$[1].cliente.id", is(10)));
    }

    @Test
    void testCrearNotificacion() throws Exception {
        Notificacion nueva = Notificacion.builder()
                .mensaje("Nuevo mensaje")
                .cliente(cliente)
                .build();

        Notificacion guardada = Notificacion.builder()
                .id(3L)
                .mensaje(nueva.getMensaje())
                .cliente(nueva.getCliente())
                .fecha(LocalDateTime.now())
                .build();

        Mockito.when(notificacionService.guardar(any(Notificacion.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/notificaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.mensaje", is("Nuevo mensaje")))
                .andExpect(jsonPath("$.cliente.id", is(10)))
                .andExpect(jsonPath("$.fecha").exists());
    }

    @Test
    void testEliminarNotificacion_Encontrado() throws Exception {
        Mockito.when(notificacionService.obtenerPorId(1L)).thenReturn(Optional.of(notificacion1));

        mockMvc.perform(delete("/api/notificaciones/1"))
                .andExpect(status().isOk());

        Mockito.verify(notificacionService).eliminar(1L);
    }

    @Test
    void testEliminarNotificacion_NoEncontrado() throws Exception {
        Mockito.when(notificacionService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/notificaciones/99"))
                .andExpect(status().isNotFound());
    }
}
