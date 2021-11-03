package sv.com.mono.cam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Precios.
 */
@Entity
@Table(name = "precios")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Precios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "precio")
    private Long precio;

    @OneToMany(mappedBy = "precios")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "detalles", "proveedores", "productos", "precios" }, allowSetters = true)
    private Set<Lotes> lotes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Precios id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public Precios fechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public Precios fechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Long getPrecio() {
        return this.precio;
    }

    public Precios precio(Long precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Set<Lotes> getLotes() {
        return this.lotes;
    }

    public Precios lotes(Set<Lotes> lotes) {
        this.setLotes(lotes);
        return this;
    }

    public Precios addLotes(Lotes lotes) {
        this.lotes.add(lotes);
        lotes.setPrecios(this);
        return this;
    }

    public Precios removeLotes(Lotes lotes) {
        this.lotes.remove(lotes);
        lotes.setPrecios(null);
        return this;
    }

    public void setLotes(Set<Lotes> lotes) {
        if (this.lotes != null) {
            this.lotes.forEach(i -> i.setPrecios(null));
        }
        if (lotes != null) {
            lotes.forEach(i -> i.setPrecios(this));
        }
        this.lotes = lotes;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
