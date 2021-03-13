package com.mkp.lacampesina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FacturasMaster.
 */
@Entity
@Table(name = "facturas_master")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FacturasMaster implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "numero_factura", nullable = false)
    private Long numeroFactura;

    @Column(name = "fecha_factura")
    private Instant fechaFactura;

    @Column(name = "condicion_pago")
    private Boolean condicionPago;

    @OneToMany(mappedBy = "clienteId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Clientes> clientes = new HashSet<>();

    @OneToMany(mappedBy = "abonoId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AbonoFacturas> abonoFacturas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "facturasMasters", allowSetters = true)
    private FacturasDetalle facturaMasterId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroFactura() {
        return numeroFactura;
    }

    public FacturasMaster numeroFactura(Long numeroFactura) {
        this.numeroFactura = numeroFactura;
        return this;
    }

    public void setNumeroFactura(Long numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Instant getFechaFactura() {
        return fechaFactura;
    }

    public FacturasMaster fechaFactura(Instant fechaFactura) {
        this.fechaFactura = fechaFactura;
        return this;
    }

    public void setFechaFactura(Instant fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Boolean isCondicionPago() {
        return condicionPago;
    }

    public FacturasMaster condicionPago(Boolean condicionPago) {
        this.condicionPago = condicionPago;
        return this;
    }

    public void setCondicionPago(Boolean condicionPago) {
        this.condicionPago = condicionPago;
    }

    public Set<Clientes> getClientes() {
        return clientes;
    }

    public FacturasMaster clientes(Set<Clientes> clientes) {
        this.clientes = clientes;
        return this;
    }

    public FacturasMaster addClientes(Clientes clientes) {
        this.clientes.add(clientes);
        clientes.setClienteId(this);
        return this;
    }

    public FacturasMaster removeClientes(Clientes clientes) {
        this.clientes.remove(clientes);
        clientes.setClienteId(null);
        return this;
    }

    public void setClientes(Set<Clientes> clientes) {
        this.clientes = clientes;
    }

    public Set<AbonoFacturas> getAbonoFacturas() {
        return abonoFacturas;
    }

    public FacturasMaster abonoFacturas(Set<AbonoFacturas> abonoFacturas) {
        this.abonoFacturas = abonoFacturas;
        return this;
    }

    public FacturasMaster addAbonoFacturas(AbonoFacturas abonoFacturas) {
        this.abonoFacturas.add(abonoFacturas);
        abonoFacturas.setAbonoId(this);
        return this;
    }

    public FacturasMaster removeAbonoFacturas(AbonoFacturas abonoFacturas) {
        this.abonoFacturas.remove(abonoFacturas);
        abonoFacturas.setAbonoId(null);
        return this;
    }

    public void setAbonoFacturas(Set<AbonoFacturas> abonoFacturas) {
        this.abonoFacturas = abonoFacturas;
    }

    public FacturasDetalle getFacturaMasterId() {
        return facturaMasterId;
    }

    public FacturasMaster facturaMasterId(FacturasDetalle facturasDetalle) {
        this.facturaMasterId = facturasDetalle;
        return this;
    }

    public void setFacturaMasterId(FacturasDetalle facturasDetalle) {
        this.facturaMasterId = facturasDetalle;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturasMaster)) {
            return false;
        }
        return id != null && id.equals(((FacturasMaster) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturasMaster{" +
            "id=" + getId() +
            ", numeroFactura=" + getNumeroFactura() +
            ", fechaFactura='" + getFechaFactura() + "'" +
            ", condicionPago='" + isCondicionPago() + "'" +
            "}";
    }
}
