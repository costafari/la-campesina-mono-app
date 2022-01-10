package sv.com.mono.cam.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.com.mono.cam.domain.*; // for static metamodels
import sv.com.mono.cam.domain.Facturas;
import sv.com.mono.cam.repository.FacturasRepository;
import sv.com.mono.cam.service.criteria.FacturasCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Facturas} entities in the database.
 * The main input is a {@link FacturasCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Facturas} or a {@link Page} of {@link Facturas} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacturasQueryService extends QueryService<Facturas> {

    private final Logger log = LoggerFactory.getLogger(FacturasQueryService.class);

    private final FacturasRepository facturasRepository;

    public FacturasQueryService(FacturasRepository facturasRepository) {
        this.facturasRepository = facturasRepository;
    }

    /**
     * Return a {@link List} of {@link Facturas} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Facturas> findByCriteria(FacturasCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Facturas> specification = createSpecification(criteria);
        return facturasRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Facturas} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Facturas> findByCriteria(FacturasCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Facturas> specification = createSpecification(criteria);
        return facturasRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacturasCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Facturas> specification = createSpecification(criteria);
        return facturasRepository.count(specification);
    }

    /**
     * Function to convert {@link FacturasCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Facturas> createSpecification(FacturasCriteria criteria) {
        Specification<Facturas> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Facturas_.id));
            }
            if (criteria.getNumeroFactura() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumeroFactura(), Facturas_.numeroFactura));
            }
            if (criteria.getFechaFactura() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaFactura(), Facturas_.fechaFactura));
            }
            if (criteria.getCondicionPago() != null) {
                specification = specification.and(buildSpecification(criteria.getCondicionPago(), Facturas_.condicionPago));
            }
            if (criteria.getClientesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClientesId(), root -> root.join(Facturas_.clientes, JoinType.LEFT).get(Clientes_.id))
                    );
            }
            if (criteria.getDetallesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDetallesId(), root -> root.join(Facturas_.detalles, JoinType.LEFT).get(Detalles_.id))
                    );
            }
            if (criteria.getAbonosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAbonosId(), root -> root.join(Facturas_.abonos, JoinType.LEFT).get(Abonos_.id))
                    );
            }
        }
        return specification;
    }
}
