package com.mkp.lacampesina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Lotes.
 */
@Entity
@Table(name = "lotes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lotes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_entrada")
    private LocalDate fechaEntrada;

    @Column(name = "lote")
    private String lote;

    @OneToMany(mappedBy = "loteId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Precios> precios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "lotes", allowSetters = true)
    private Productos productoId;

    @ManyToOne
    @JsonIgnoreProperties(value = "lotes", allowSetters = true)
    private Proveedores proveedorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Lotes cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public Lotes fechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
        return this;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getLote() {
        return lote;
    }

    public Lotes lote(String lote) {
        this.lote = lote;
        return this;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Set<Precios> getPrecios() {
        return precios;
    }

    public Lotes precios(Set<Precios> precios) {
        this.precios = precios;
        return this;
    }

    public Lotes addPrecios(Precios precios) {
        this.precios.add(precios);
        precios.setLoteId(this);
        return this;
    }

    public Lotes removePrecios(Precios precios) {
        this.precios.remove(precios);
        precios.setLoteId(null);
        return this;
    }

    public void setPrecios(Set<Precios> precios) {
        this.precios = precios;
    }

    public Productos getProductoId() {
        return productoId;
    }

    public Lotes productoId(Productos productos) {
        this.productoId = productos;
        return this;
    }

    public void setProductoId(Productos productos) {
        this.productoId = productos;
    }

    public Proveedores getProveedorId() {
        return proveedorId;
    }

    public Lotes proveedorId(Proveedores proveedores) {
        this.proveedorId = proveedores;
        return this;
    }

    public void setProveedorId(Proveedores proveedores) {
        this.proveedorId = proveedores;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lotes)) {
            return false;
        }
        return id != null && id.equals(((Lotes) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lotes{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", fechaEntrada='" + getFechaEntrada() + "'" +
            ", lote='" + getLote() + "'" +
            "}";
    }
}
