package com.mkp.lacampesina.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Tickets.
 */
@Entity
@Table(name = "tickets")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tickets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad")
    private Long cantidad;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "fecha_expedicion")
    private Instant fechaExpedicion;

    @Column(name = "total")
    private Long total;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public Tickets cantidad(Long cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Tickets createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getFechaExpedicion() {
        return fechaExpedicion;
    }

    public Tickets fechaExpedicion(Instant fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
        return this;
    }

    public void setFechaExpedicion(Instant fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Long getTotal() {
        return total;
    }

    public Tickets total(Long total) {
        this.total = total;
        return this;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tickets)) {
            return false;
        }
        return id != null && id.equals(((Tickets) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tickets{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", fechaExpedicion='" + getFechaExpedicion() + "'" +
            ", total=" + getTotal() +
            "}";
    }
}
