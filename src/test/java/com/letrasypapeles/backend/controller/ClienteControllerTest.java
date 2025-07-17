package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente1;
    private Cliente cliente2;

    @BeforeEach
    void setup() {
        cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNombre("Juan");
        cliente1.setContraseña("secret1");

        cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNombre("Maria");
        cliente2.setContraseña("secret2");
    }

    @Test
    @WithMockUser
    void obtenerTodos_debeRetornarListaClientes() throws Exception {
        Mockito.when(clienteService.obtenerTodos()).thenReturn(Arrays.asList(cliente1, cliente2));

        mockMvc.perform(get("/api/clientes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.clienteList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.clienteList[0].nombre", is("Juan")))
        .andExpect(jsonPath("$._embedded.clienteList[1].nombre", is("Maria")));

    }

    @Test
    void obtenerPorId_existente_debeRetornarClienteSinContraseña() throws Exception {
        Mockito.when(clienteService.obtenerPorId(1L)).thenReturn(Optional.of(cliente1));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.contraseña").value(nullValue()));
    }

    @Test
    void obtenerPorId_noExistente_debeRetornar404() throws Exception {
        Mockito.when(clienteService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registrarCliente_debeGuardarYRetornarClienteSinContraseña() throws Exception {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setId(3L);
        nuevoCliente.setNombre("Pedro");
        nuevoCliente.setContraseña("secret3");

        Mockito.when(clienteService.registrarCliente(any(Cliente.class))).thenReturn(nuevoCliente);

        mockMvc.perform(post("/api/clientes/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoCliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nombre", is("Pedro")))
                .andExpect(jsonPath("$.contraseña").value(nullValue()));
    }

    @Test
    void actualizarCliente_existente_debeActualizarYRetornarClienteSinContraseña() throws Exception {
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(1L);
        clienteActualizado.setNombre("Juan Actualizado");
        clienteActualizado.setContraseña("secretActualizada");

        Mockito.when(clienteService.obtenerPorId(1L)).thenReturn(Optional.of(cliente1));
        Mockito.when(clienteService.actualizarCliente(any(Cliente.class))).thenReturn(clienteActualizado);

        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Actualizado")))
                .andExpect(jsonPath("$.contraseña").value(nullValue()));
    }

    @Test
    void actualizarCliente_noExistente_debeRetornar404() throws Exception {
        Mockito.when(clienteService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/clientes/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarCliente_existente_debeRetornarOk() throws Exception {
        Mockito.when(clienteService.obtenerPorId(1L)).thenReturn(Optional.of(cliente1));

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isOk());

        Mockito.verify(clienteService).eliminar(1L);
    }

    @Test
    void eliminarCliente_noExistente_debeRetornar404() throws Exception {
        Mockito.when(clienteService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/clientes/99"))
                .andExpect(status().isNotFound());
    }
}
