package com.letrasypapeles.backend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ReservaTest {

    @Test
    void reservaShouldReferenceProductoAndCliente() {
        Producto producto = Producto.builder().nombre("Goma").build();
        Cliente cliente = Cliente.builder().nombre("Ana").build();

        Reserva reserva = Reserva.builder()
            .producto(producto)
            .cliente(cliente)
            .build();

        assertEquals("Goma", reserva.getProducto().getNombre());
        assertEquals("Ana", reserva.getCliente().getNombre());
    }

    
}
