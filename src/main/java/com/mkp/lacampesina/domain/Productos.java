package com.mkp.lacampesina.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Productos.
 */
@Entity
@Table(name = "productos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Productos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descipcion")
    private String descipcion;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "notas")
    private String notas;

    @OneToMany(mappedBy = "productoId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Lotes> lotes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public Productos descipcion(String descipcion) {
        this.descipcion = descipcion;
        return this;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    public String getNombre() {
        return nombre;
    }

    public Productos nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNotas() {
        return notas;
    }

    public Productos notas(String notas) {
        this.notas = notas;
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Set<Lotes> getLotes() {
        return lotes;
    }

    public Productos lotes(Set<Lotes> lotes) {
        this.lotes = lotes;
        return this;
    }

    public Productos addLotes(Lotes lotes) {
        this.lotes.add(lotes);
        lotes.setProductoId(this);
        return this;
    }

    public Productos removeLotes(Lotes lotes) {
        this.lotes.remove(lotes);
        lotes.setProductoId(null);
        return this;
    }

    public void setLotes(Set<Lotes> lotes) {
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
        return 31;
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
