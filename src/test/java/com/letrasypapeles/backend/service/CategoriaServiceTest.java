package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Categoria;
import com.letrasypapeles.backend.repository.CategoriaRepository;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void obtenerTodas_debeRetornarListaCategorias() {
        List<Categoria> categoriasMock = List.of(new Categoria(), new Categoria());
        when(categoriaRepository.findAll()).thenReturn(categoriasMock);

        List<Categoria> categorias = categoriaService.obtenerTodas();

        assertNotNull(categorias);
        assertEquals(2, categorias.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_conIdExistente_debeRetornarCategoria() {
        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaMock));

        Optional<Categoria> resultado = categoriaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_conIdNoExistente_debeRetornarVacio() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Categoria> resultado = categoriaService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());
        verify(categoriaRepository, times(1)).findById(99L);
    }

    @Test
    void guardar_debeGuardarYRetornarCategoria() {
        Categoria categoriaMock = new Categoria();
        when(categoriaRepository.save(categoriaMock)).thenReturn(categoriaMock);

        Categoria resultado = categoriaService.guardar(categoriaMock);

        assertNotNull(resultado);
        verify(categoriaRepository, times(1)).save(categoriaMock);
    }

    @Test
    void eliminar_debeEliminarCategoriaPorId() {
        doNothing().when(categoriaRepository).deleteById(1L);

        categoriaService.eliminar(1L);

        verify(categoriaRepository, times(1)).deleteById(1L);
    }
}
