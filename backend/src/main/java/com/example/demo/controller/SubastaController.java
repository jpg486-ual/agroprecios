package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Subasta;
import com.example.demo.repository.SubastaRepository;

@RestController
@RequestMapping("/subastas")
public class SubastaController {

    private final SubastaRepository subastaRepository;

    public SubastaController(SubastaRepository subastaRepository) {
        this.subastaRepository = subastaRepository;
    }

    @GetMapping
    public List<Subasta> getSubastas() {
        return subastaRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subasta> getSubastaById(@PathVariable Long id) {
        return subastaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}