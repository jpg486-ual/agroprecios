package com.example.demo.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "preciosubasta",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "subasta_id", "fecha", "producto_id", "corte" })
        })
public class PrecioSubasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subasta_id", nullable = false)
    private Long subastaId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subasta_id", insertable = false, updatable = false)
    private Subasta subasta;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer corte;

    @Column(nullable = false)
    private Integer precio;

    public PrecioSubasta() {
    }

    public PrecioSubasta(Long subastaId, LocalDate fecha, Long productoId, Integer corte, Integer precio) {
        this.subastaId = subastaId;
        this.fecha = fecha;
        this.productoId = productoId;
        this.corte = corte;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    @JsonProperty("subasta_id")
    public Long getSubastaId() {
        return subastaId;
    }

    @JsonProperty("subasta_id")
    public void setSubastaId(Long subastaId) {
        this.subastaId = subastaId;
    }

    public Subasta getSubasta() {
        return subasta;
    }

    public void setSubasta(Subasta subasta) {
        this.subasta = subasta;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @JsonProperty("producto_id")
    public Long getProductoId() {
        return productoId;
    }

    @JsonProperty("producto_id")
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCorte() {
        return corte;
    }

    public void setCorte(Integer corte) {
        this.corte = corte;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }
}