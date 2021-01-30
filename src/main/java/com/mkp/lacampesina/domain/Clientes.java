package com.mkp.lacampesina.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Clientes.
 */
@Entity
@Table(name = "clientes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Clientes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activo")
    private Integer activo;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "direcion")
    private String direcion;

    @Column(name = "email")
    private String email;

    @Column(name = "nombre_contacto")
    private String nombreContacto;

    @Column(name = "nombre_empresa")
    private String nombreEmpresa;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "notas")
    private String notas;

    @Column(name = "sitio_web")
    private String sitioWeb;

    @Column(name = "telefono_fijo")
    private String telefonoFijo;

    @Column(name = "telefono_fijo_2")
    private String telefonoFijo2;

    @Column(name = "telefono_movil")
    private String telefonoMovil;

    @Column(name = "telefono_movil_2")
    private String telefonoMovil2;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActivo() {
        return activo;
    }

    public Clientes activo(Integer activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getApellidos() {
        return apellidos;
    }

    public Clientes apellidos(String apellidos) {
        this.apellidos = apellidos;
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Clientes createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDirecion() {
        return direcion;
    }

    public Clientes direcion(String direcion) {
        this.direcion = direcion;
        return this;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public String getEmail() {
        return email;
    }

    public Clientes email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public Clientes nombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
        return this;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public Clientes nombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
        return this;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombres() {
        return nombres;
    }

    public Clientes nombres(String nombres) {
        this.nombres = nombres;
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNotas() {
        return notas;
    }

    public Clientes notas(String notas) {
        this.notas = notas;
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public Clientes sitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
        return this;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public Clientes telefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
        return this;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoFijo2() {
        return telefonoFijo2;
    }

    public Clientes telefonoFijo2(String telefonoFijo2) {
        this.telefonoFijo2 = telefonoFijo2;
        return this;
    }

    public void setTelefonoFijo2(String telefonoFijo2) {
        this.telefonoFijo2 = telefonoFijo2;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public Clientes telefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
        return this;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getTelefonoMovil2() {
        return telefonoMovil2;
    }

    public Clientes telefonoMovil2(String telefonoMovil2) {
        this.telefonoMovil2 = telefonoMovil2;
        return this;
    }

    public void setTelefonoMovil2(String telefonoMovil2) {
        this.telefonoMovil2 = telefonoMovil2;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Clientes)) {
            return false;
        }
        return id != null && id.equals(((Clientes) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Clientes{" +
            "id=" + getId() +
            ", activo=" + getActivo() +
            ", apellidos='" + getApellidos() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", direcion='" + getDirecion() + "'" +
            ", email='" + getEmail() + "'" +
            ", nombreContacto='" + getNombreContacto() + "'" +
            ", nombreEmpresa='" + getNombreEmpresa() + "'" +
            ", nombres='" + getNombres() + "'" +
            ", notas='" + getNotas() + "'" +
            ", sitioWeb='" + getSitioWeb() + "'" +
            ", telefonoFijo='" + getTelefonoFijo() + "'" +
            ", telefonoFijo2='" + getTelefonoFijo2() + "'" +
            ", telefonoMovil='" + getTelefonoMovil() + "'" +
            ", telefonoMovil2='" + getTelefonoMovil2() + "'" +
            "}";
    }
}
