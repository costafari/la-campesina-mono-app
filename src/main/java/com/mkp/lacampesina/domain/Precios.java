package com.mkp.lacampesina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Precios.
 */
@Entity
@Table(name = "precios")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Precios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "precio")
    private Long precio;

    @ManyToOne
    @JsonIgnoreProperties(value = "precios", allowSetters = true)
    private Lotes loteId;

    @ManyToOne
    @JsonIgnoreProperties(value = "precios", allowSetters = true)
    private Clientes clienteId;

    @ManyToOne
    @JsonIgnoreProperties(value = "precios", allowSetters = true)
    private FacturasDetalle precioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public Precios fechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public Precios fechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Long getPrecio() {
        return precio;
    }

    public Precios precio(Long precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Lotes getLoteId() {
        return loteId;
    }

    public Precios loteId(Lotes lotes) {
        this.loteId = lotes;
        return this;
    }

    public void setLoteId(Lotes lotes) {
        this.loteId = lotes;
    }

    public Clientes getClienteId() {
        return clienteId;
    }

    public Precios clienteId(Clientes clientes) {
        this.clienteId = clientes;
        return this;
    }

    public void setClienteId(Clientes clientes) {
        this.clienteId = clientes;
    }

    public FacturasDetalle getPrecioId() {
        return precioId;
    }

    public Precios precioId(FacturasDetalle facturasDetalle) {
        this.precioId = facturasDetalle;
        return this;
    }

    public void setPrecioId(FacturasDetalle facturasDetalle) {
        this.precioId = facturasDetalle;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Precios)) {
            return false;
        }
        return id != null && id.equals(((Precios) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Precios{" +
            "id=" + getId() +
            ", fechaFin='" + getFechaFin() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", precio=" + getPrecio() +
            "}";
    }
}
