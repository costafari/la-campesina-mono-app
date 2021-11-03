package sv.com.mono.cam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Detalles.
 */
@Entity
@Table(name = "detalles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Detalles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Long cantidad;

    @Column(name = "total")
    private Long total;

    @ManyToOne
    @JsonIgnoreProperties(value = { "clientes", "detalles", "abonos" }, allowSetters = true)
    private Facturas facturas;

    @ManyToOne
    @JsonIgnoreProperties(value = { "detalles", "proveedores", "productos", "precios" }, allowSetters = true)
    private Lotes lotes;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Detalles id(Long id) {
        this.id = id;
        return this;
    }

    public Long getCantidad() {
        return this.cantidad;
    }

    public Detalles cantidad(Long cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getTotal() {
        return this.total;
    }

    public Detalles total(Long total) {
        this.total = total;
        return this;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Facturas getFacturas() {
        return this.facturas;
    }

    public Detalles facturas(Facturas facturas) {
        this.setFacturas(facturas);
        return this;
    }

    public void setFacturas(Facturas facturas) {
        this.facturas = facturas;
    }

    public Lotes getLotes() {
        return this.lotes;
    }

    public Detalles lotes(Lotes lotes) {
        this.setLotes(lotes);
        return this;
    }

    public void setLotes(Lotes lotes) {
        this.lotes = lotes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Detalles)) {
            return false;
        }
        return id != null && id.equals(((Detalles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Detalles{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", total=" + getTotal() +
            "}";
    }
}
