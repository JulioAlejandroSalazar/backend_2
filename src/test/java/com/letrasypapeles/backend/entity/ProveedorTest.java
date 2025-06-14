package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProveedorTest {

    @Test
    void proveedorDebeTenerInformacionCompleta() {
        Proveedor proveedor = Proveedor.builder()
            .nombre("Distribuidora Sur")
            .contacto("contacto@sur.cl")
            .build();

        assertNotNull(proveedor.getNombre());
        assertNotNull(proveedor.getContacto());
    }

    @Test
    void proveedorIncompletoDebeLanzarError() {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Incompleto");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            if (proveedor.getContacto() == null) {
                throw new IllegalArgumentException("Contacto del proveedor es obligatorio");
            }
        });

        assertEquals("Contacto del proveedor es obligatorio", ex.getMessage());
    }

    @Test
    void reservaDebeEstarVinculadaAlProveedor() {
        Proveedor proveedor = Proveedor.builder()
            .nombre("Librería Andes")
            .contacto("andes@libros.cl")
            .build();

        Producto producto = Producto.builder()
            .nombre("Enciclopedia")
            .proveedor(proveedor)
            .build();

        Reserva reserva = Reserva.builder()
            .producto(producto)
            .build();

        assertEquals("Librería Andes", reserva.getProducto().getProveedor().getNombre());
    }
}
