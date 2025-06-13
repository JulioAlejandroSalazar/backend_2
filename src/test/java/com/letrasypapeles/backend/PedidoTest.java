package com.letrasypapeles.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.entity.Producto;

public class PedidoTest {

    @Test
    void pedidoShouldBeLinkedToCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Julio");

        Pedido pedido = Pedido.builder().cliente(cliente).build();

        assertEquals("Julio", pedido.getCliente().getNombre());
    }

    @Test
    void pedidoCanContainMultipleProducts() {
        Producto p1 = new Producto();
        p1.setNombre("Lapiz");

        Producto p2 = new Producto();
        p2.setNombre("Cuaderno");

        Pedido pedido = Pedido.builder()
            .listaProductos(List.of(p1, p2))
            .build();

        assertEquals(2, pedido.getListaProductos().size());
    }

    
}
