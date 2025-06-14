package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.repository.SucursalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    @Test
    void obtenerTodas_debeRetornarListaDeSucursales() {
        List<Sucursal> sucursalesMock = List.of(
                Sucursal.builder().id(1L).nombre("Sucursal 1").build(),
                Sucursal.builder().id(2L).nombre("Sucursal 2").build()
        );
    
        when(sucursalRepository.findAll()).thenReturn(sucursalesMock);
    
        List<Sucursal> resultado = sucursalService.obtenerTodas();
    
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(sucursalRepository, times(1)).findAll();
    }
    
    @Test
    void obtenerPorId_conIdExistente_debeRetornarSucursal() {
        Long id = 1L;
        Sucursal sucursalMock = Sucursal.builder().id(id).nombre("Sucursal 1").build();
    
        when(sucursalRepository.findById(id)).thenReturn(Optional.of(sucursalMock));
    
        Optional<Sucursal> resultado = sucursalService.obtenerPorId(id);
    
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        verify(sucursalRepository, times(1)).findById(id);
    }
    
    @Test
    void guardar_debeRetornarSucursalGuardada() {
        Sucursal sucursal = Sucursal.builder().nombre("Sucursal Nueva").build();
        Sucursal sucursalGuardada = Sucursal.builder().id(1L).nombre("Sucursal Nueva").build();
    
        when(sucursalRepository.save(sucursal)).thenReturn(sucursalGuardada);
    
        Sucursal resultado = sucursalService.guardar(sucursal);
    
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Sucursal Nueva", resultado.getNombre());
        verify(sucursalRepository, times(1)).save(sucursal);
    }
    

    @Test
    void eliminar_debeLlamarDeleteByIdUnaVez() {
        Long id = 1L;

        doNothing().when(sucursalRepository).deleteById(id);

        sucursalService.eliminar(id);

        verify(sucursalRepository, times(1)).deleteById(id);
    }
}
