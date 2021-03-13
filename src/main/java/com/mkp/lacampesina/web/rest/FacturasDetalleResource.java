package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.domain.FacturasDetalle;
import com.mkp.lacampesina.repository.FacturasDetalleRepository;
import com.mkp.lacampesina.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.mkp.lacampesina.domain.FacturasDetalle}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FacturasDetalleResource {
    private final Logger log = LoggerFactory.getLogger(FacturasDetalleResource.class);

    private static final String ENTITY_NAME = "facturasDetalle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacturasDetalleRepository facturasDetalleRepository;

    public FacturasDetalleResource(FacturasDetalleRepository facturasDetalleRepository) {
        this.facturasDetalleRepository = facturasDetalleRepository;
    }

    /**
     * {@code POST  /facturas-detalles} : Create a new facturasDetalle.
     *
     * @param facturasDetalle the facturasDetalle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facturasDetalle, or with status {@code 400 (Bad Request)} if the facturasDetalle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facturas-detalles")
    public ResponseEntity<FacturasDetalle> createFacturasDetalle(@Valid @RequestBody FacturasDetalle facturasDetalle)
        throws URISyntaxException {
        log.debug("REST request to save FacturasDetalle : {}", facturasDetalle);
        if (facturasDetalle.getId() != null) {
            throw new BadRequestAlertException("A new facturasDetalle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacturasDetalle result = facturasDetalleRepository.save(facturasDetalle);
        return ResponseEntity
            .created(new URI("/api/facturas-detalles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facturas-detalles} : Updates an existing facturasDetalle.
     *
     * @param facturasDetalle the facturasDetalle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturasDetalle,
     * or with status {@code 400 (Bad Request)} if the facturasDetalle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facturasDetalle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facturas-detalles")
    public ResponseEntity<FacturasDetalle> updateFacturasDetalle(@Valid @RequestBody FacturasDetalle facturasDetalle)
        throws URISyntaxException {
        log.debug("REST request to update FacturasDetalle : {}", facturasDetalle);
        if (facturasDetalle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FacturasDetalle result = facturasDetalleRepository.save(facturasDetalle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturasDetalle.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /facturas-detalles} : get all the facturasDetalles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facturasDetalles in body.
     */
    @GetMapping("/facturas-detalles")
    public List<FacturasDetalle> getAllFacturasDetalles() {
        log.debug("REST request to get all FacturasDetalles");
        return facturasDetalleRepository.findAll();
    }

    /**
     * {@code GET  /facturas-detalles/:id} : get the "id" facturasDetalle.
     *
     * @param id the id of the facturasDetalle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facturasDetalle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facturas-detalles/{id}")
    public ResponseEntity<FacturasDetalle> getFacturasDetalle(@PathVariable Long id) {
        log.debug("REST request to get FacturasDetalle : {}", id);
        Optional<FacturasDetalle> facturasDetalle = facturasDetalleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(facturasDetalle);
    }

    /**
     * {@code DELETE  /facturas-detalles/:id} : delete the "id" facturasDetalle.
     *
     * @param id the id of the facturasDetalle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facturas-detalles/{id}")
    public ResponseEntity<Void> deleteFacturasDetalle(@PathVariable Long id) {
        log.debug("REST request to delete FacturasDetalle : {}", id);
        facturasDetalleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
