package sv.com.mono.cam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Productos.
 */
@Entity
@Table(name = "productos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Productos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "descipcion")
    private String descipcion;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "notas")
    private String notas;

    @OneToMany(mappedBy = "productos")
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

    public Productos id(Long id) {
        this.id = id;
        return this;
    }

    public String getDescipcion() {
        return this.descipcion;
    }

    public Productos descipcion(String descipcion) {
        this.descipcion = descipcion;
        return this;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Productos nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNotas() {
        return this.notas;
    }

    public Productos notas(String notas) {
        this.notas = notas;
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Set<Lotes> getLotes() {
        return this.lotes;
    }

    public Productos lotes(Set<Lotes> lotes) {
        this.setLotes(lotes);
        return this;
    }

    public Productos addLotes(Lotes lotes) {
        this.lotes.add(lotes);
        lotes.setProductos(this);
        return this;
    }

    public Productos removeLotes(Lotes lotes) {
        this.lotes.remove(lotes);
        lotes.setProductos(null);
        return this;
    }

    public void setLotes(Set<Lotes> lotes) {
        if (this.lotes != null) {
            this.lotes.forEach(i -> i.setProductos(null));
        }
        if (lotes != null) {
            lotes.forEach(i -> i.setProductos(this));
        }
        this.lotes = lotes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Productos)) {
            return false;
        }
        return id != null && id.equals(((Productos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Productos{" +
            "id=" + getId() +
            ", descipcion='" + getDescipcion() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", notas='" + getNotas() + "'" +
            "}";
    }
}
