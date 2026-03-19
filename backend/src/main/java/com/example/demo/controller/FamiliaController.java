package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Familia;
import com.example.demo.repository.FamiliaRepository;

@RestController
@RequestMapping("/familias")
public class FamiliaController {

    private final FamiliaRepository familiaRepository;

    public FamiliaController(FamiliaRepository familiaRepository) {
        this.familiaRepository = familiaRepository;
    }

    @GetMapping
    public List<Familia> getFamilias() {
        return familiaRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}