package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerTodos() {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerTodos().stream()
                .map(pedido -> EntityModel.of(pedido,
                        linkTo(methodOn(PedidoController.class).obtenerPorId(pedido.getId())).withSelfRel(),
                        linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("pedidos")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(pedido -> EntityModel.of(pedido,
                        linkTo(methodOn(PedidoController.class).obtenerPorId(id)).withSelfRel(),
                        linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("todos"),
                        linkTo(methodOn(PedidoController.class).eliminarPedido(id)).withRel("eliminar"),
                        linkTo(methodOn(PedidoController.class).actualizarPedido(id, pedido)).withRel("actualizar")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerPorClienteId(@PathVariable Long clienteId) {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerPorClienteId(clienteId).stream()
                .map(pedido -> EntityModel.of(pedido,
                        linkTo(methodOn(PedidoController.class).obtenerPorId(pedido.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerPorClienteId(clienteId)).withSelfRel()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Pedido>> crearPedido(@RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoService.guardar(pedido);
        return ResponseEntity.ok(EntityModel.of(nuevoPedido,
                linkTo(methodOn(PedidoController.class).obtenerPorId(nuevoPedido.getId())).withSelfRel(),
                linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("todos")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        return pedidoService.obtenerPorId(id)
                .map(p -> {
                    pedido.setId(id);
                    Pedido actualizado = pedidoService.guardar(pedido);
                    return ResponseEntity.ok(EntityModel.of(actualizado,
                            linkTo(methodOn(PedidoController.class).obtenerPorId(id)).withSelfRel(),
                            linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("todos")));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(p -> {
                    pedidoService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
