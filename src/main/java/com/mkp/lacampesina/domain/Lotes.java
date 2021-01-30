package com.mkp.lacampesina.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

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

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "fecha_entrada")
    private Instant fechaEntrada;

    @Column(name = "lote")
    private String lote;

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Lotes createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getFechaEntrada() {
        return fechaEntrada;
    }

    public Lotes fechaEntrada(Instant fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
        return this;
    }

    public void setFechaEntrada(Instant fechaEntrada) {
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
            ", createdAt='" + getCreatedAt() + "'" +
            ", fechaEntrada='" + getFechaEntrada() + "'" +
            ", lote='" + getLote() + "'" +
            "}";
    }
}
