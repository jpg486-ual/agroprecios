package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.PrecioSubasta;
import com.example.demo.repository.PrecioSubastaRepository;

@RestController
@RequestMapping("/preciosubasta")
public class PrecioSubastaController {

    private final PrecioSubastaRepository precioSubastaRepository;

    public PrecioSubastaController(PrecioSubastaRepository precioSubastaRepository) {
        this.precioSubastaRepository = precioSubastaRepository;
    }

    @GetMapping
    public List<PrecioSubasta> getPreciosBySubasta(
            @RequestParam(name = "subasta_id", required = false) Long subastaId) {
        if (subastaId == null) {
            return precioSubastaRepository.findAll(
                    Sort.by(Sort.Direction.ASC, "subastaId", "fecha", "productoId", "corte"));
        }
        return precioSubastaRepository.findBySubastaIdOrderByProductoIdAscCorteAsc(subastaId);
    }
}