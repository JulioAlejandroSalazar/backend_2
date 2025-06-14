package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encoded-password");
    }

    @Test
    void testGuardarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.registrarCliente(cliente);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testBuscarPorId() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Ana");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.obtenerPorId(1L);

        assertTrue(result.isPresent());
        assertEquals("Ana", result.get().getNombre());
        verify(clienteRepository).findById(1L);
    }

    @Test
    void testObtenerTodos() {
        Cliente c1 = new Cliente();
        Cliente c2 = new Cliente();

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Cliente> clientes = clienteService.obtenerTodos();

        assertEquals(2, clientes.size());
        verify(clienteRepository).findAll();
    }

    @Test
    void testEliminarCliente() {
        Long id = 3L;

        clienteService.eliminar(id);

        verify(clienteRepository).deleteById(id);
    }
}
