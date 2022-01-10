package sv.com.mono.cam.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link sv.com.mono.cam.domain.Detalles} entity. This class is used
 * in {@link sv.com.mono.cam.web.rest.DetallesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /detalles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DetallesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter cantidad;

    private LongFilter total;

    private LongFilter facturasId;

    private LongFilter lotesId;

    public DetallesCriteria() {}

    public DetallesCriteria(DetallesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.facturasId = other.facturasId == null ? null : other.facturasId.copy();
        this.lotesId = other.lotesId == null ? null : other.lotesId.copy();
    }

    @Override
    public DetallesCriteria copy() {
        return new DetallesCriteria(this);
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

    public LongFilter getCantidad() {
        return cantidad;
    }

    public LongFilter cantidad() {
        if (cantidad == null) {
            cantidad = new LongFilter();
        }
        return cantidad;
    }

    public void setCantidad(LongFilter cantidad) {
        this.cantidad = cantidad;
    }

    public LongFilter getTotal() {
        return total;
    }

    public LongFilter total() {
        if (total == null) {
            total = new LongFilter();
        }
        return total;
    }

    public void setTotal(LongFilter total) {
        this.total = total;
    }

    public LongFilter getFacturasId() {
        return facturasId;
    }

    public LongFilter facturasId() {
        if (facturasId == null) {
            facturasId = new LongFilter();
        }
        return facturasId;
    }

    public void setFacturasId(LongFilter facturasId) {
        this.facturasId = facturasId;
    }

    public LongFilter getLotesId() {
        return lotesId;
    }

    public LongFilter lotesId() {
        if (lotesId == null) {
            lotesId = new LongFilter();
        }
        return lotesId;
    }

    public void setLotesId(LongFilter lotesId) {
        this.lotesId = lotesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DetallesCriteria that = (DetallesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(total, that.total) &&
            Objects.equals(facturasId, that.facturasId) &&
            Objects.equals(lotesId, that.lotesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidad, total, facturasId, lotesId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetallesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (facturasId != null ? "facturasId=" + facturasId + ", " : "") +
            (lotesId != null ? "lotesId=" + lotesId + ", " : "") +
            "}";
    }
}
