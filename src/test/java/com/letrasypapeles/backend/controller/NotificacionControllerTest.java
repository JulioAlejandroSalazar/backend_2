package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.service.NotificacionService;
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
import static org.mockito.ArgumentMatchers.any;
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

    private Notificacion crearNotificacionMock() {
        return Notificacion.builder()
                .id(1L)
                .fecha(LocalDateTime.now())
                .build();
    }

    @Test
    void obtenerTodas_deberiaRetornarListaNotificaciones() throws Exception {
        Mockito.when(notificacionService.obtenerTodas()).thenReturn(Arrays.asList(crearNotificacionMock()));

        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_existente_deberiaRetornarNotificacion() throws Exception {
        Mockito.when(notificacionService.obtenerPorId(1L)).thenReturn(Optional.of(crearNotificacionMock()));

        mockMvc.perform(get("/api/notificaciones/1"))
                .andExpect(status().isOk());
    }

    @Test
    void crearNotificacion_deberiaRetornarNotificacionCreada() throws Exception {
        Notificacion notificacion = crearNotificacionMock();
        notificacion.setId(null);

        Mockito.when(notificacionService.guardar(any(Notificacion.class))).thenReturn(crearNotificacionMock());

        mockMvc.perform(post("/api/notificaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificacion)))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarNotificacion_existente_deberiaRetornarOk() throws Exception {
        Mockito.when(notificacionService.obtenerPorId(1L)).thenReturn(Optional.of(crearNotificacionMock()));

        mockMvc.perform(delete("/api/notificaciones/1"))
                .andExpect(status().isOk());
    }
}
