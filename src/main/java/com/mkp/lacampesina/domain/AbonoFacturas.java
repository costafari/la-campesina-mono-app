package com.mkp.lacampesina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AbonoFacturas.
 */
@Entity
@Table(name = "abono_facturas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AbonoFacturas implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "saldo_anterior", nullable = false)
    private Long saldoAnterior;

    @NotNull
    @Column(name = "abono", nullable = false)
    private Long abono;

    @Column(name = "nuevo_saldo")
    private Long nuevoSaldo;

    @ManyToOne
    @JsonIgnoreProperties(value = "abonoFacturas", allowSetters = true)
    private FacturasMaster abonoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSaldoAnterior() {
        return saldoAnterior;
    }

    public AbonoFacturas saldoAnterior(Long saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
        return this;
    }

    public void setSaldoAnterior(Long saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public Long getAbono() {
        return abono;
    }

    public AbonoFacturas abono(Long abono) {
        this.abono = abono;
        return this;
    }

    public void setAbono(Long abono) {
        this.abono = abono;
    }

    public Long getNuevoSaldo() {
        return nuevoSaldo;
    }

    public AbonoFacturas nuevoSaldo(Long nuevoSaldo) {
        this.nuevoSaldo = nuevoSaldo;
        return this;
    }

    public void setNuevoSaldo(Long nuevoSaldo) {
        this.nuevoSaldo = nuevoSaldo;
    }

    public FacturasMaster getAbonoId() {
        return abonoId;
    }

    public AbonoFacturas abonoId(FacturasMaster facturasMaster) {
        this.abonoId = facturasMaster;
        return this;
    }

    public void setAbonoId(FacturasMaster facturasMaster) {
        this.abonoId = facturasMaster;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbonoFacturas)) {
            return false;
        }
        return id != null && id.equals(((AbonoFacturas) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AbonoFacturas{" +
            "id=" + getId() +
            ", saldoAnterior=" + getSaldoAnterior() +
            ", abono=" + getAbono() +
            ", nuevoSaldo=" + getNuevoSaldo() +
            "}";
    }
}
