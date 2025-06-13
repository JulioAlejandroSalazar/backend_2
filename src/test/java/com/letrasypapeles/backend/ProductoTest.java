package com.letrasypapeles.backend;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.letrasypapeles.backend.entity.Producto;

public class ProductoTest {
    
    @Test
    void productoPrecioShouldBePositive() {
        Producto producto = Producto.builder().precio(new BigDecimal("1990")).build();
        assertTrue(producto.getPrecio().compareTo(BigDecimal.ZERO) > 0);
    }

}
