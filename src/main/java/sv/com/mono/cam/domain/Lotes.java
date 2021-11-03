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
 * A Lotes.
 */
@Entity
@Table(name = "lotes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lotes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_entrada")
    private LocalDate fechaEntrada;

    @Column(name = "lote")
    private String lote;

    @OneToMany(mappedBy = "lotes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "facturas", "lotes" }, allowSetters = true)
    private Set<Detalles> detalles = new HashSet<>();

    @JsonIgnoreProperties(value = { "lotes" }, allowSetters = true)
    @OneToOne(mappedBy = "lotes")
    private Proveedores proveedores;

    @ManyToOne
    @JsonIgnoreProperties(value = { "lotes" }, allowSetters = true)
    private Productos productos;

    @ManyToOne
    @JsonIgnoreProperties(value = { "lotes" }, allowSetters = true)
    private Precios precios;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lotes id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Lotes cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFechaEntrada() {
        return this.fechaEntrada;
    }

    public Lotes fechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
        return this;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getLote() {
        return this.lote;
    }

    public Lotes lote(String lote) {
        this.lote = lote;
        return this;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Set<Detalles> getDetalles() {
        return this.detalles;
    }

    public Lotes detalles(Set<Detalles> detalles) {
        this.setDetalles(detalles);
        return this;
    }

    public Lotes addDetalles(Detalles detalles) {
        this.detalles.add(detalles);
        detalles.setLotes(this);
        return this;
    }

    public Lotes removeDetalles(Detalles detalles) {
        this.detalles.remove(detalles);
        detalles.setLotes(null);
        return this;
    }

    public void setDetalles(Set<Detalles> detalles) {
        if (this.detalles != null) {
            this.detalles.forEach(i -> i.setLotes(null));
        }
        if (detalles != null) {
            detalles.forEach(i -> i.setLotes(this));
        }
        this.detalles = detalles;
    }

    public Proveedores getProveedores() {
        return this.proveedores;
    }

    public Lotes proveedores(Proveedores proveedores) {
        this.setProveedores(proveedores);
        return this;
    }

    public void setProveedores(Proveedores proveedores) {
        if (this.proveedores != null) {
            this.proveedores.setLotes(null);
        }
        if (proveedores != null) {
            proveedores.setLotes(this);
        }
        this.proveedores = proveedores;
    }

    public Productos getProductos() {
        return this.productos;
    }

    public Lotes productos(Productos productos) {
        this.setProductos(productos);
        return this;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    public Precios getPrecios() {
        return this.precios;
    }

    public Lotes precios(Precios precios) {
        this.setPrecios(precios);
        return this;
    }

    public void setPrecios(Precios precios) {
        this.precios = precios;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lotes{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", fechaEntrada='" + getFechaEntrada() + "'" +
            ", lote='" + getLote() + "'" +
            "}";
    }
}
