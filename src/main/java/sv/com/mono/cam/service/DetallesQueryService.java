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
import sv.com.mono.cam.domain.Detalles;
import sv.com.mono.cam.repository.DetallesRepository;
import sv.com.mono.cam.service.criteria.DetallesCriteria;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Detalles} entities in the database.
 * The main input is a {@link DetallesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Detalles} or a {@link Page} of {@link Detalles} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DetallesQueryService extends QueryService<Detalles> {

    private final Logger log = LoggerFactory.getLogger(DetallesQueryService.class);

    private final DetallesRepository detallesRepository;

    public DetallesQueryService(DetallesRepository detallesRepository) {
        this.detallesRepository = detallesRepository;
    }

    /**
     * Return a {@link List} of {@link Detalles} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Detalles> findByCriteria(DetallesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Detalles> specification = createSpecification(criteria);
        return detallesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Detalles} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Detalles> findByCriteria(DetallesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Detalles> specification = createSpecification(criteria);
        return detallesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DetallesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Detalles> specification = createSpecification(criteria);
        return detallesRepository.count(specification);
    }

    /**
     * Function to convert {@link DetallesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Detalles> createSpecification(DetallesCriteria criteria) {
        Specification<Detalles> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Detalles_.id));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), Detalles_.cantidad));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), Detalles_.total));
            }
            if (criteria.getFacturasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFacturasId(), root -> root.join(Detalles_.facturas, JoinType.LEFT).get(Facturas_.id))
                    );
            }
            if (criteria.getLotesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLotesId(), root -> root.join(Detalles_.lotes, JoinType.LEFT).get(Lotes_.id))
                    );
            }
        }
        return specification;
    }
}
