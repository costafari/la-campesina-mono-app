package sv.com.mono.cam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Clientes.
 */
@Entity
@Table(name = "clientes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Clientes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "apellidos")
    private String apellidos;

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
    private Integer telefonoFijo;

    @Column(name = "telefono_fijo_2")
    private Integer telefonoFijo2;

    @Column(name = "telefono_movil")
    private Integer telefonoMovil;

    @Column(name = "telefono_movil_2")
    private Integer telefonoMovil2;

    @OneToMany(mappedBy = "clientes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "clientes", "detalles", "abonos" }, allowSetters = true)
    private Set<Facturas> facturas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clientes id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Clientes activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Clientes apellidos(String apellidos) {
        this.apellidos = apellidos;
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDirecion() {
        return this.direcion;
    }

    public Clientes direcion(String direcion) {
        this.direcion = direcion;
        return this;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public String getEmail() {
        return this.email;
    }

    public Clientes email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreContacto() {
        return this.nombreContacto;
    }

    public Clientes nombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
        return this;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getNombreEmpresa() {
        return this.nombreEmpresa;
    }

    public Clientes nombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
        return this;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Clientes nombres(String nombres) {
        this.nombres = nombres;
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNotas() {
        return this.notas;
    }

    public Clientes notas(String notas) {
        this.notas = notas;
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getSitioWeb() {
        return this.sitioWeb;
    }

    public Clientes sitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
        return this;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public Integer getTelefonoFijo() {
        return this.telefonoFijo;
    }

    public Clientes telefonoFijo(Integer telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
        return this;
    }

    public void setTelefonoFijo(Integer telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public Integer getTelefonoFijo2() {
        return this.telefonoFijo2;
    }

    public Clientes telefonoFijo2(Integer telefonoFijo2) {
        this.telefonoFijo2 = telefonoFijo2;
        return this;
    }

    public void setTelefonoFijo2(Integer telefonoFijo2) {
        this.telefonoFijo2 = telefonoFijo2;
    }

    public Integer getTelefonoMovil() {
        return this.telefonoMovil;
    }

    public Clientes telefonoMovil(Integer telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
        return this;
    }

    public void setTelefonoMovil(Integer telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public Integer getTelefonoMovil2() {
        return this.telefonoMovil2;
    }

    public Clientes telefonoMovil2(Integer telefonoMovil2) {
        this.telefonoMovil2 = telefonoMovil2;
        return this;
    }

    public void setTelefonoMovil2(Integer telefonoMovil2) {
        this.telefonoMovil2 = telefonoMovil2;
    }

    public Set<Facturas> getFacturas() {
        return this.facturas;
    }

    public Clientes facturas(Set<Facturas> facturas) {
        this.setFacturas(facturas);
        return this;
    }

    public Clientes addFacturas(Facturas facturas) {
        this.facturas.add(facturas);
        facturas.setClientes(this);
        return this;
    }

    public Clientes removeFacturas(Facturas facturas) {
        this.facturas.remove(facturas);
        facturas.setClientes(null);
        return this;
    }

    public void setFacturas(Set<Facturas> facturas) {
        if (this.facturas != null) {
            this.facturas.forEach(i -> i.setClientes(null));
        }
        if (facturas != null) {
            facturas.forEach(i -> i.setClientes(this));
        }
        this.facturas = facturas;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Clientes{" +
            "id=" + getId() +
            ", activo='" + getActivo() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", direcion='" + getDirecion() + "'" +
            ", email='" + getEmail() + "'" +
            ", nombreContacto='" + getNombreContacto() + "'" +
            ", nombreEmpresa='" + getNombreEmpresa() + "'" +
            ", nombres='" + getNombres() + "'" +
            ", notas='" + getNotas() + "'" +
            ", sitioWeb='" + getSitioWeb() + "'" +
            ", telefonoFijo=" + getTelefonoFijo() +
            ", telefonoFijo2=" + getTelefonoFijo2() +
            ", telefonoMovil=" + getTelefonoMovil() +
            ", telefonoMovil2=" + getTelefonoMovil2() +
            "}";
    }
}
