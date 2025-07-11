package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> obtenerTodos() {
        List<EntityModel<Cliente>> clientes = clienteService.obtenerTodos().stream()
                .map(cliente -> {
                    cliente.setContraseña(null);
                    return EntityModel.of(cliente,
                            linkTo(methodOn(ClienteController.class).obtenerPorId(cliente.getId())).withSelfRel());
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(cliente -> {
                    cliente.setContraseña(null);
                    return EntityModel.of(cliente,
                            linkTo(methodOn(ClienteController.class).obtenerPorId(id)).withSelfRel(),
                            linkTo(methodOn(ClienteController.class).obtenerTodos()).withRel("todos"),
                            linkTo(methodOn(ClienteController.class).eliminarCliente(id)).withRel("eliminar"),
                            linkTo(methodOn(ClienteController.class).actualizarCliente(id, cliente)).withRel("actualizar"));
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registro")
    public ResponseEntity<EntityModel<Cliente>> registrarCliente(@RequestBody Cliente cliente) {
        Cliente nuevo = clienteService.registrarCliente(cliente);
        nuevo.setContraseña(null);
        return ResponseEntity.ok(EntityModel.of(nuevo,
                linkTo(methodOn(ClienteController.class).obtenerPorId(nuevo.getId())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).obtenerTodos()).withRel("todos")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.obtenerPorId(id)
                .map(c -> {
                    cliente.setId(id);
                    Cliente actualizado = clienteService.actualizarCliente(cliente);
                    actualizado.setContraseña(null);
                    return ResponseEntity.ok(EntityModel.of(actualizado,
                            linkTo(methodOn(ClienteController.class).obtenerPorId(id)).withSelfRel(),
                            linkTo(methodOn(ClienteController.class).obtenerTodos()).withRel("todos")));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(c -> {
                    clienteService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
