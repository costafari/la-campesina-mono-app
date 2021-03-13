package com.mkp.lacampesina.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FacturasDetalle.
 */
@Entity
@Table(name = "facturas_detalle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FacturasDetalle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Long cantidad;

    @Column(name = "total")
    private Long total;

    @OneToMany(mappedBy = "facturaMasterId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<FacturasMaster> facturasMasters = new HashSet<>();

    @OneToMany(mappedBy = "loteId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Lotes> lotes = new HashSet<>();

    @OneToMany(mappedBy = "precioId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Precios> precios = new HashSet<>();

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

    public FacturasDetalle cantidad(Long cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getTotal() {
        return total;
    }

    public FacturasDetalle total(Long total) {
        this.total = total;
        return this;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Set<FacturasMaster> getFacturasMasters() {
        return facturasMasters;
    }

    public FacturasDetalle facturasMasters(Set<FacturasMaster> facturasMasters) {
        this.facturasMasters = facturasMasters;
        return this;
    }

    public FacturasDetalle addFacturasMaster(FacturasMaster facturasMaster) {
        this.facturasMasters.add(facturasMaster);
        facturasMaster.setFacturaMasterId(this);
        return this;
    }

    public FacturasDetalle removeFacturasMaster(FacturasMaster facturasMaster) {
        this.facturasMasters.remove(facturasMaster);
        facturasMaster.setFacturaMasterId(null);
        return this;
    }

    public void setFacturasMasters(Set<FacturasMaster> facturasMasters) {
        this.facturasMasters = facturasMasters;
    }

    public Set<Lotes> getLotes() {
        return lotes;
    }

    public FacturasDetalle lotes(Set<Lotes> lotes) {
        this.lotes = lotes;
        return this;
    }

    public FacturasDetalle addLotes(Lotes lotes) {
        this.lotes.add(lotes);
        lotes.setLoteId(this);
        return this;
    }

    public FacturasDetalle removeLotes(Lotes lotes) {
        this.lotes.remove(lotes);
        lotes.setLoteId(null);
        return this;
    }

    public void setLotes(Set<Lotes> lotes) {
        this.lotes = lotes;
    }

    public Set<Precios> getPrecios() {
        return precios;
    }

    public FacturasDetalle precios(Set<Precios> precios) {
        this.precios = precios;
        return this;
    }

    public FacturasDetalle addPrecios(Precios precios) {
        this.precios.add(precios);
        precios.setPrecioId(this);
        return this;
    }

    public FacturasDetalle removePrecios(Precios precios) {
        this.precios.remove(precios);
        precios.setPrecioId(null);
        return this;
    }

    public void setPrecios(Set<Precios> precios) {
        this.precios = precios;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturasDetalle)) {
            return false;
        }
        return id != null && id.equals(((FacturasDetalle) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturasDetalle{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", total=" + getTotal() +
            "}";
    }
}
