package sv.com.mono.cam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Proveedores.
 */
@Entity
@Table(name = "proveedores")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Proveedores implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "nombre_contacto")
    private String nombreContacto;

    @Column(name = "nombre_empresa")
    private String nombreEmpresa;

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

    @JsonIgnoreProperties(value = { "detalles", "proveedores", "productos", "precios" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Lotes lotes;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proveedores id(Long id) {
        this.id = id;
        return this;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Proveedores direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreContacto() {
        return this.nombreContacto;
    }

    public Proveedores nombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
        return this;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getNombreEmpresa() {
        return this.nombreEmpresa;
    }

    public Proveedores nombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
        return this;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNotas() {
        return this.notas;
    }

    public Proveedores notas(String notas) {
        this.notas = notas;
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getSitioWeb() {
        return this.sitioWeb;
    }

    public Proveedores sitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
        return this;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public Integer getTelefonoFijo() {
        return this.telefonoFijo;
    }

    public Proveedores telefonoFijo(Integer telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
        return this;
    }

    public void setTelefonoFijo(Integer telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public Integer getTelefonoFijo2() {
        return this.telefonoFijo2;
    }

    public Proveedores telefonoFijo2(Integer telefonoFijo2) {
        this.telefonoFijo2 = telefonoFijo2;
        return this;
    }

    public void setTelefonoFijo2(Integer telefonoFijo2) {
        this.telefonoFijo2 = telefonoFijo2;
    }

    public Integer getTelefonoMovil() {
        return this.telefonoMovil;
    }

    public Proveedores telefonoMovil(Integer telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
        return this;
    }

    public void setTelefonoMovil(Integer telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public Integer getTelefonoMovil2() {
        return this.telefonoMovil2;
    }

    public Proveedores telefonoMovil2(Integer telefonoMovil2) {
        this.telefonoMovil2 = telefonoMovil2;
        return this;
    }

    public void setTelefonoMovil2(Integer telefonoMovil2) {
        this.telefonoMovil2 = telefonoMovil2;
    }

    public Lotes getLotes() {
        return this.lotes;
    }

    public Proveedores lotes(Lotes lotes) {
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
        if (!(o instanceof Proveedores)) {
            return false;
        }
        return id != null && id.equals(((Proveedores) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proveedores{" +
            "id=" + getId() +
            ", direccion='" + getDireccion() + "'" +
            ", nombreContacto='" + getNombreContacto() + "'" +
            ", nombreEmpresa='" + getNombreEmpresa() + "'" +
            ", notas='" + getNotas() + "'" +
            ", sitioWeb='" + getSitioWeb() + "'" +
            ", telefonoFijo=" + getTelefonoFijo() +
            ", telefonoFijo2=" + getTelefonoFijo2() +
            ", telefonoMovil=" + getTelefonoMovil() +
            ", telefonoMovil2=" + getTelefonoMovil2() +
            "}";
    }
}
