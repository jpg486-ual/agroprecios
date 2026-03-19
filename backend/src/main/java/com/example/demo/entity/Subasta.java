package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "subastas")
public class Subasta {

    @Id
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @JsonIgnore
    @OneToMany(mappedBy = "subasta")
    private List<PrecioSubasta> preciosSubasta = new ArrayList<>();

    public Subasta() {
    }

    public Subasta(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<PrecioSubasta> getPreciosSubasta() {
        return preciosSubasta;
    }

    public void setPreciosSubasta(List<PrecioSubasta> preciosSubasta) {
        this.preciosSubasta = preciosSubasta;
    }
}