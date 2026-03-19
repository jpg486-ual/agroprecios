package com.example.demo.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Familia;
import com.example.demo.entity.PrecioSubasta;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Subasta;
import com.example.demo.repository.FamiliaRepository;
import com.example.demo.repository.PrecioSubastaRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.SubastaRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Profile("!test")
public class SeedDataLoader implements CommandLineRunner {

    private final SubastaRepository subastaRepository;
    private final FamiliaRepository familiaRepository;
    private final ProductoRepository productoRepository;
    private final PrecioSubastaRepository precioSubastaRepository;
    private final ObjectMapper objectMapper;

    public SeedDataLoader(
            SubastaRepository subastaRepository,
            FamiliaRepository familiaRepository,
            ProductoRepository productoRepository,
            PrecioSubastaRepository precioSubastaRepository,
            ObjectMapper objectMapper) {
        this.subastaRepository = subastaRepository;
        this.familiaRepository = familiaRepository;
        this.productoRepository = productoRepository;
        this.precioSubastaRepository = precioSubastaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (subastaRepository.count() > 0 || familiaRepository.count() > 0
                || productoRepository.count() > 0 || precioSubastaRepository.count() > 0) {
            return;
        }

        SeedPayload payload = readSeedPayload();

        subastaRepository.saveAll(payload.subastas().stream()
                .map(item -> new Subasta(item.id(), item.nombre()))
                .toList());

        familiaRepository.saveAll(payload.familias().stream()
                .map(item -> new Familia(item.id(), item.nombre()))
                .toList());

        productoRepository.saveAll(payload.productos().stream()
                .map(item -> new Producto(item.id(), item.familiaId(), item.nombre(), item.url()))
                .toList());

        precioSubastaRepository.saveAll(payload.preciosubasta().stream()
                .map(item -> new PrecioSubasta(
                        item.subastaId(),
                        LocalDate.parse(item.fecha()),
                        item.productoId(),
                        item.corte(),
                        item.precio()))
                .toList());
    }

    private SeedPayload readSeedPayload() throws IOException {
        ClassPathResource resource = new ClassPathResource("seed/db.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, SeedPayload.class);
        }
    }

    private record SeedPayload(
            List<SubastaSeed> subastas,
            List<FamiliaSeed> familias,
            List<ProductoSeed> productos,
            List<PrecioSubastaSeed> preciosubasta) {
    }

    private record SubastaSeed(Long id, String nombre) {
    }

    private record FamiliaSeed(Long id, String nombre) {
    }

    private record ProductoSeed(
            Long id,
            @JsonProperty("familia_id") Long familiaId,
            String nombre,
            String url) {
    }

    private record PrecioSubastaSeed(
            @JsonProperty("subasta_id") Long subastaId,
            String fecha,
            @JsonProperty("producto_id") Long productoId,
            Integer corte,
            Integer precio) {
    }
}