package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();

        List<EntityModel<Producto>> productosModel = productos.stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).obtenerPorId(producto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("productos")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(productosModel,
                        linkTo(methodOn(ProductoController.class).obtenerTodos()).withSelfRel())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).obtenerPorId(id)).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("todos"),
                        linkTo(methodOn(ProductoController.class).eliminarProducto(id)).withRel("eliminar"),
                        linkTo(methodOn(ProductoController.class).actualizarProducto(id, producto)).withRel("actualizar")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<Producto>> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.ok(
                EntityModel.of(nuevoProducto,
                        linkTo(methodOn(ProductoController.class).obtenerPorId(nuevoProducto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("todos"))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.obtenerPorId(id)
                .map(p -> {
                    producto.setId(id);
                    Producto productoActualizado = productoService.guardar(producto);
                    return ResponseEntity.ok(
                            EntityModel.of(productoActualizado,
                                    linkTo(methodOn(ProductoController.class).obtenerPorId(id)).withSelfRel(),
                                    linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("todos"))
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(p -> {
                    productoService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
