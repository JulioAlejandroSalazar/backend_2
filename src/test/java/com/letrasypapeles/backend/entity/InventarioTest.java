package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventarioTest {

    @Test
    void testInventarioBuilderAndGetters() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Cuaderno")
                .build();

        Sucursal sucursal = Sucursal.builder()
                .id(2L)
                .nombre("Sucursal Central")
                .build();

        Inventario inventario = Inventario.builder()
                .id(10L)
                .cantidad(50)
                .umbral(20)
                .producto(producto)
                .sucursal(sucursal)
                .build();

        assertEquals(10L, inventario.getId());
        assertEquals(50, inventario.getCantidad());
        assertEquals(20, inventario.getUmbral());
        assertEquals(producto, inventario.getProducto());
        assertEquals(sucursal, inventario.getSucursal());
    }

    @Test
    void testSettersAndGetters() {
        Inventario inventario = new Inventario();
        Producto producto = new Producto();
        Sucursal sucursal = new Sucursal();

        inventario.setId(5L);
        inventario.setCantidad(100);
        inventario.setUmbral(10);
        inventario.setProducto(producto);
        inventario.setSucursal(sucursal);

        assertEquals(5L, inventario.getId());
        assertEquals(100, inventario.getCantidad());
        assertEquals(10, inventario.getUmbral());
        assertSame(producto, inventario.getProducto());
        assertSame(sucursal, inventario.getSucursal());
    }
}
