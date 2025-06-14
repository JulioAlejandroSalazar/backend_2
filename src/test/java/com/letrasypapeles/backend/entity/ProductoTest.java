package com.letrasypapeles.backend.entity;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductoTest {
    
    @Test
    void productoPrecioShouldBePositive() {
        Producto producto = Producto.builder().precio(new BigDecimal("1990")).build();
        assertTrue(producto.getPrecio().compareTo(BigDecimal.ZERO) > 0);
    }

}
