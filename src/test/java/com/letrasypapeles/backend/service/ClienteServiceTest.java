package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        List<Cliente> lista = Arrays.asList(
            new Cliente(), new Cliente()
        );
        when(clienteRepository.findAll()).thenReturn(lista);

        List<Cliente> resultado = clienteService.obtenerTodos();

        assertEquals(2, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_Encontrado() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(clienteRepository).findById(1L);
    }

    @Test
    void testObtenerPorEmail_Encontrado() {
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        when(clienteRepository.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.obtenerPorEmail("test@example.com");

        assertTrue(resultado.isPresent());
        assertEquals("test@example.com", resultado.get().getEmail());
        verify(clienteRepository).findByEmail("test@example.com");
    }

    @Test
    void testRegistrarCliente_CorreoNoExiste() {
        Cliente cliente = new Cliente();
        cliente.setEmail("nuevo@example.com");
        cliente.setContraseña("1234");

        when(clienteRepository.existsByEmail("nuevo@example.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("encodedPassword");
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente resultado = clienteService.registrarCliente(cliente);

        assertEquals("encodedPassword", resultado.getContraseña());
        assertEquals(0, resultado.getPuntosFidelidad());
        assertTrue(resultado.getRoles().contains(Role.CLIENTE));

        verify(clienteRepository).existsByEmail("nuevo@example.com");
        verify(passwordEncoder).encode("1234");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testRegistrarCliente_CorreoExiste_LanzaError() {
        Cliente cliente = new Cliente();
        cliente.setEmail("existente@example.com");

        when(clienteRepository.existsByEmail("existente@example.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> clienteService.registrarCliente(cliente));

        assertEquals("El correo electrónico ya está registrado.", ex.getMessage());

        verify(clienteRepository).existsByEmail("existente@example.com");
        verify(clienteRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void testActualizarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.actualizarCliente(cliente);

        assertEquals(cliente, resultado);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testEliminar() {
        Long id = 1L;

        doNothing().when(clienteRepository).deleteById(id);

        clienteService.eliminar(id);

        verify(clienteRepository).deleteById(id);
    }
}
