package com.mkp.lacampesina.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Generalidades.
 */
@Entity
@Table(name = "generalidades")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Generalidades implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "nombre_empresa")
    private String nombreEmpresa;

    @Column(name = "nombre_propietario")
    private String nombrePropietario;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaInicio() {
        return fechaInicio;
    }

    public Generalidades fechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public Generalidades nombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
        return this;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public Generalidades nombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
        return this;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Generalidades)) {
            return false;
        }
        return id != null && id.equals(((Generalidades) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Generalidades{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", nombreEmpresa='" + getNombreEmpresa() + "'" +
            ", nombrePropietario='" + getNombrePropietario() + "'" +
            "}";
    }
}
