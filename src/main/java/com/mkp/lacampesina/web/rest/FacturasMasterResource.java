package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.domain.FacturasMaster;
import com.mkp.lacampesina.repository.FacturasMasterRepository;
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
 * REST controller for managing {@link com.mkp.lacampesina.domain.FacturasMaster}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FacturasMasterResource {
    private final Logger log = LoggerFactory.getLogger(FacturasMasterResource.class);

    private static final String ENTITY_NAME = "facturasMaster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacturasMasterRepository facturasMasterRepository;

    public FacturasMasterResource(FacturasMasterRepository facturasMasterRepository) {
        this.facturasMasterRepository = facturasMasterRepository;
    }

    /**
     * {@code POST  /facturas-masters} : Create a new facturasMaster.
     *
     * @param facturasMaster the facturasMaster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facturasMaster, or with status {@code 400 (Bad Request)} if the facturasMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facturas-masters")
    public ResponseEntity<FacturasMaster> createFacturasMaster(@Valid @RequestBody FacturasMaster facturasMaster)
        throws URISyntaxException {
        log.debug("REST request to save FacturasMaster : {}", facturasMaster);
        if (facturasMaster.getId() != null) {
            throw new BadRequestAlertException("A new facturasMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacturasMaster result = facturasMasterRepository.save(facturasMaster);
        return ResponseEntity
            .created(new URI("/api/facturas-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facturas-masters} : Updates an existing facturasMaster.
     *
     * @param facturasMaster the facturasMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturasMaster,
     * or with status {@code 400 (Bad Request)} if the facturasMaster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facturasMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facturas-masters")
    public ResponseEntity<FacturasMaster> updateFacturasMaster(@Valid @RequestBody FacturasMaster facturasMaster)
        throws URISyntaxException {
        log.debug("REST request to update FacturasMaster : {}", facturasMaster);
        if (facturasMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FacturasMaster result = facturasMasterRepository.save(facturasMaster);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturasMaster.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /facturas-masters} : get all the facturasMasters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facturasMasters in body.
     */
    @GetMapping("/facturas-masters")
    public List<FacturasMaster> getAllFacturasMasters() {
        log.debug("REST request to get all FacturasMasters");
        return facturasMasterRepository.findAll();
    }

    /**
     * {@code GET  /facturas-masters/:id} : get the "id" facturasMaster.
     *
     * @param id the id of the facturasMaster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facturasMaster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facturas-masters/{id}")
    public ResponseEntity<FacturasMaster> getFacturasMaster(@PathVariable Long id) {
        log.debug("REST request to get FacturasMaster : {}", id);
        Optional<FacturasMaster> facturasMaster = facturasMasterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(facturasMaster);
    }

    /**
     * {@code DELETE  /facturas-masters/:id} : delete the "id" facturasMaster.
     *
     * @param id the id of the facturasMaster to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facturas-masters/{id}")
    public ResponseEntity<Void> deleteFacturasMaster(@PathVariable Long id) {
        log.debug("REST request to delete FacturasMaster : {}", id);
        facturasMasterRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
