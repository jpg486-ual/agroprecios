package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Producto;
import com.example.demo.repository.ProductoRepository;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public List<Producto> getProductos() {
        return productoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("/custom/search")
    public List<Producto> searchProductosByNombre(
            @RequestParam(name = "nombre") String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(nombre);
    }
}