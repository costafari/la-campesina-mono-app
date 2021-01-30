package com.mkp.lacampesina.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

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

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "fecha_fin")
    private Instant fechaFin;

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "precio")
    private Long precio;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Precios createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getFechaFin() {
        return fechaFin;
    }

    public Precios fechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
        return this;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Instant getFechaInicio() {
        return fechaInicio;
    }

    public Precios fechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
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
            ", createdAt='" + getCreatedAt() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", precio=" + getPrecio() +
            "}";
    }
}
