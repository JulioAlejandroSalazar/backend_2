package com.letrasypapeles.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.entity.Reserva;

public class ReservaTest {

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
