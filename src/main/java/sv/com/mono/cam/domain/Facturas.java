package sv.com.mono.cam.domain;

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
 * A Facturas.
 */
@Entity
@Table(name = "facturas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Facturas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "numero_factura", nullable = false)
    private Long numeroFactura;

    @Column(name = "fecha_factura")
    private Instant fechaFactura;

    @Column(name = "condicion_pago")
    private Boolean condicionPago;

    @ManyToOne
    @JsonIgnoreProperties(value = { "facturas" }, allowSetters = true)
    private Clientes clientes;

    @OneToMany(mappedBy = "facturas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "facturas", "lotes" }, allowSetters = true)
    private Set<Detalles> detalles = new HashSet<>();

    @OneToMany(mappedBy = "facturas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "facturas" }, allowSetters = true)
    private Set<Abonos> abonos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Facturas id(Long id) {
        this.id = id;
        return this;
    }

    public Long getNumeroFactura() {
        return this.numeroFactura;
    }

    public Facturas numeroFactura(Long numeroFactura) {
        this.numeroFactura = numeroFactura;
        return this;
    }

    public void setNumeroFactura(Long numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Instant getFechaFactura() {
        return this.fechaFactura;
    }

    public Facturas fechaFactura(Instant fechaFactura) {
        this.fechaFactura = fechaFactura;
        return this;
    }

    public void setFechaFactura(Instant fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Boolean getCondicionPago() {
        return this.condicionPago;
    }

    public Facturas condicionPago(Boolean condicionPago) {
        this.condicionPago = condicionPago;
        return this;
    }

    public void setCondicionPago(Boolean condicionPago) {
        this.condicionPago = condicionPago;
    }

    public Clientes getClientes() {
        return this.clientes;
    }

    public Facturas clientes(Clientes clientes) {
        this.setClientes(clientes);
        return this;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public Set<Detalles> getDetalles() {
        return this.detalles;
    }

    public Facturas detalles(Set<Detalles> detalles) {
        this.setDetalles(detalles);
        return this;
    }

    public Facturas addDetalles(Detalles detalles) {
        this.detalles.add(detalles);
        detalles.setFacturas(this);
        return this;
    }

    public Facturas removeDetalles(Detalles detalles) {
        this.detalles.remove(detalles);
        detalles.setFacturas(null);
        return this;
    }

    public void setDetalles(Set<Detalles> detalles) {
        if (this.detalles != null) {
            this.detalles.forEach(i -> i.setFacturas(null));
        }
        if (detalles != null) {
            detalles.forEach(i -> i.setFacturas(this));
        }
        this.detalles = detalles;
    }

    public Set<Abonos> getAbonos() {
        return this.abonos;
    }

    public Facturas abonos(Set<Abonos> abonos) {
        this.setAbonos(abonos);
        return this;
    }

    public Facturas addAbonos(Abonos abonos) {
        this.abonos.add(abonos);
        abonos.setFacturas(this);
        return this;
    }

    public Facturas removeAbonos(Abonos abonos) {
        this.abonos.remove(abonos);
        abonos.setFacturas(null);
        return this;
    }

    public void setAbonos(Set<Abonos> abonos) {
        if (this.abonos != null) {
            this.abonos.forEach(i -> i.setFacturas(null));
        }
        if (abonos != null) {
            abonos.forEach(i -> i.setFacturas(this));
        }
        this.abonos = abonos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facturas)) {
            return false;
        }
        return id != null && id.equals(((Facturas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facturas{" +
            "id=" + getId() +
            ", numeroFactura=" + getNumeroFactura() +
            ", fechaFactura='" + getFechaFactura() + "'" +
            ", condicionPago='" + getCondicionPago() + "'" +
            "}";
    }
}
