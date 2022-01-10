package sv.com.mono.cam.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link sv.com.mono.cam.domain.Facturas} entity. This class is used
 * in {@link sv.com.mono.cam.web.rest.FacturasResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FacturasCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter numeroFactura;

    private InstantFilter fechaFactura;

    private BooleanFilter condicionPago;

    private LongFilter clientesId;

    private LongFilter detallesId;

    private LongFilter abonosId;

    public FacturasCriteria() {}

    public FacturasCriteria(FacturasCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.numeroFactura = other.numeroFactura == null ? null : other.numeroFactura.copy();
        this.fechaFactura = other.fechaFactura == null ? null : other.fechaFactura.copy();
        this.condicionPago = other.condicionPago == null ? null : other.condicionPago.copy();
        this.clientesId = other.clientesId == null ? null : other.clientesId.copy();
        this.detallesId = other.detallesId == null ? null : other.detallesId.copy();
        this.abonosId = other.abonosId == null ? null : other.abonosId.copy();
    }

    @Override
    public FacturasCriteria copy() {
        return new FacturasCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getNumeroFactura() {
        return numeroFactura;
    }

    public LongFilter numeroFactura() {
        if (numeroFactura == null) {
            numeroFactura = new LongFilter();
        }
        return numeroFactura;
    }

    public void setNumeroFactura(LongFilter numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public InstantFilter getFechaFactura() {
        return fechaFactura;
    }

    public InstantFilter fechaFactura() {
        if (fechaFactura == null) {
            fechaFactura = new InstantFilter();
        }
        return fechaFactura;
    }

    public void setFechaFactura(InstantFilter fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public BooleanFilter getCondicionPago() {
        return condicionPago;
    }

    public BooleanFilter condicionPago() {
        if (condicionPago == null) {
            condicionPago = new BooleanFilter();
        }
        return condicionPago;
    }

    public void setCondicionPago(BooleanFilter condicionPago) {
        this.condicionPago = condicionPago;
    }

    public LongFilter getClientesId() {
        return clientesId;
    }

    public LongFilter clientesId() {
        if (clientesId == null) {
            clientesId = new LongFilter();
        }
        return clientesId;
    }

    public void setClientesId(LongFilter clientesId) {
        this.clientesId = clientesId;
    }

    public LongFilter getDetallesId() {
        return detallesId;
    }

    public LongFilter detallesId() {
        if (detallesId == null) {
            detallesId = new LongFilter();
        }
        return detallesId;
    }

    public void setDetallesId(LongFilter detallesId) {
        this.detallesId = detallesId;
    }

    public LongFilter getAbonosId() {
        return abonosId;
    }

    public LongFilter abonosId() {
        if (abonosId == null) {
            abonosId = new LongFilter();
        }
        return abonosId;
    }

    public void setAbonosId(LongFilter abonosId) {
        this.abonosId = abonosId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FacturasCriteria that = (FacturasCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroFactura, that.numeroFactura) &&
            Objects.equals(fechaFactura, that.fechaFactura) &&
            Objects.equals(condicionPago, that.condicionPago) &&
            Objects.equals(clientesId, that.clientesId) &&
            Objects.equals(detallesId, that.detallesId) &&
            Objects.equals(abonosId, that.abonosId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroFactura, fechaFactura, condicionPago, clientesId, detallesId, abonosId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturasCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (numeroFactura != null ? "numeroFactura=" + numeroFactura + ", " : "") +
            (fechaFactura != null ? "fechaFactura=" + fechaFactura + ", " : "") +
            (condicionPago != null ? "condicionPago=" + condicionPago + ", " : "") +
            (clientesId != null ? "clientesId=" + clientesId + ", " : "") +
            (detallesId != null ? "detallesId=" + detallesId + ", " : "") +
            (abonosId != null ? "abonosId=" + abonosId + ", " : "") +
            "}";
    }
}
