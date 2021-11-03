package sv.com.mono.cam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Abonos.
 */
@Entity
@Table(name = "abonos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Abonos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
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
    @JsonIgnoreProperties(value = { "clientes", "detalles", "abonos" }, allowSetters = true)
    private Facturas facturas;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Abonos id(Long id) {
        this.id = id;
        return this;
    }

    public Long getSaldoAnterior() {
        return this.saldoAnterior;
    }

    public Abonos saldoAnterior(Long saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
        return this;
    }

    public void setSaldoAnterior(Long saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public Long getAbono() {
        return this.abono;
    }

    public Abonos abono(Long abono) {
        this.abono = abono;
        return this;
    }

    public void setAbono(Long abono) {
        this.abono = abono;
    }

    public Long getNuevoSaldo() {
        return this.nuevoSaldo;
    }

    public Abonos nuevoSaldo(Long nuevoSaldo) {
        this.nuevoSaldo = nuevoSaldo;
        return this;
    }

    public void setNuevoSaldo(Long nuevoSaldo) {
        this.nuevoSaldo = nuevoSaldo;
    }

    public Facturas getFacturas() {
        return this.facturas;
    }

    public Abonos facturas(Facturas facturas) {
        this.setFacturas(facturas);
        return this;
    }

    public void setFacturas(Facturas facturas) {
        this.facturas = facturas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Abonos)) {
            return false;
        }
        return id != null && id.equals(((Abonos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Abonos{" +
            "id=" + getId() +
            ", saldoAnterior=" + getSaldoAnterior() +
            ", abono=" + getAbono() +
            ", nuevoSaldo=" + getNuevoSaldo() +
            "}";
    }
}
