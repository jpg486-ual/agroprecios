package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    private Long id;

    @Column(name = "familia_id", nullable = false)
    private Long familiaId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "familia_id", insertable = false, updatable = false)
    private Familia familia;

    @Column(nullable = false)
    private String nombre;

    private String url;

    @JsonIgnore
    @OneToMany(mappedBy = "producto")
    private List<PrecioSubasta> preciosSubasta = new ArrayList<>();

    public Producto() {
    }

    public Producto(Long id, Long familiaId, String nombre, String url) {
        this.id = id;
        this.familiaId = familiaId;
        this.nombre = nombre;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("familia_id")
    public Long getFamiliaId() {
        return familiaId;
    }

    @JsonProperty("familia_id")
    public void setFamiliaId(Long familiaId) {
        this.familiaId = familiaId;
    }

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<PrecioSubasta> getPreciosSubasta() {
        return preciosSubasta;
    }

    public void setPreciosSubasta(List<PrecioSubasta> preciosSubasta) {
        this.preciosSubasta = preciosSubasta;
    }
}