package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Categoria;
import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto crearProductoMock() {
        return Producto.builder()
                .id(1L)
                .nombre("Notebook")
                .descripcion("Laptop de 15 pulgadas")
                .precio(new BigDecimal("899.99"))
                .stock(5)
                .categoria(new Categoria(1L, "Tecnología"))
                .proveedor(new Proveedor(1L, "Proveedor S.A.", "contacto@proveedor.com"))
                .build();
    }

    @Test
    void obtenerTodos_deberiaRetornarListaProductos() throws Exception {
        Mockito.when(productoService.obtenerTodos()).thenReturn(Arrays.asList(crearProductoMock()));

        mockMvc.perform(get("/api/productos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.productoList[0].nombre").value("Notebook"));

    }

    @Test
    void obtenerPorId_existente_deberiaRetornarProducto() throws Exception {
        Mockito.when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(crearProductoMock()));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Laptop de 15 pulgadas"));
    }

    @Test
    void crearProducto_deberiaRetornarProductoCreado() throws Exception {
        Producto producto = crearProductoMock();
        producto.setId(null);

        Mockito.when(productoService.guardar(any(Producto.class))).thenReturn(crearProductoMock());

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Notebook"));
    }

    @Test
    void actualizarProducto_existente_deberiaRetornarProductoActualizado() throws Exception {
        Producto actualizado = crearProductoMock();
        actualizado.setDescripcion("Actualizada");

        Mockito.when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(crearProductoMock()));
        Mockito.when(productoService.guardar(any(Producto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Actualizada"));
    }

    @Test
    void eliminarProducto_existente_deberiaRetornarOk() throws Exception {
        Mockito.when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(crearProductoMock()));

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarProducto_noExistente_deberiaRetornarNotFound() throws Exception {
        Mockito.when(productoService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/productos/999"))
                .andExpect(status().isNotFound());
    }
}
