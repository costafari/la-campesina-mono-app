package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.domain.Proveedores;
import com.mkp.lacampesina.repository.ProveedoresRepository;
import com.mkp.lacampesina.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mkp.lacampesina.domain.Proveedores}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProveedoresResource {

    private final Logger log = LoggerFactory.getLogger(ProveedoresResource.class);

    private static final String ENTITY_NAME = "proveedores";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProveedoresRepository proveedoresRepository;

    public ProveedoresResource(ProveedoresRepository proveedoresRepository) {
        this.proveedoresRepository = proveedoresRepository;
    }

    /**
     * {@code POST  /proveedores} : Create a new proveedores.
     *
     * @param proveedores the proveedores to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proveedores, or with status {@code 400 (Bad Request)} if the proveedores has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proveedores")
    public ResponseEntity<Proveedores> createProveedores(@RequestBody Proveedores proveedores) throws URISyntaxException {
        log.debug("REST request to save Proveedores : {}", proveedores);
        if (proveedores.getId() != null) {
            throw new BadRequestAlertException("A new proveedores cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Proveedores result = proveedoresRepository.save(proveedores);
        return ResponseEntity.created(new URI("/api/proveedores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proveedores} : Updates an existing proveedores.
     *
     * @param proveedores the proveedores to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proveedores,
     * or with status {@code 400 (Bad Request)} if the proveedores is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proveedores couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proveedores")
    public ResponseEntity<Proveedores> updateProveedores(@RequestBody Proveedores proveedores) throws URISyntaxException {
        log.debug("REST request to update Proveedores : {}", proveedores);
        if (proveedores.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Proveedores result = proveedoresRepository.save(proveedores);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proveedores.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /proveedores} : get all the proveedores.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proveedores in body.
     */
    @GetMapping("/proveedores")
    public List<Proveedores> getAllProveedores() {
        log.debug("REST request to get all Proveedores");
        return proveedoresRepository.findAll();
    }

    /**
     * {@code GET  /proveedores/:id} : get the "id" proveedores.
     *
     * @param id the id of the proveedores to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proveedores, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proveedores/{id}")
    public ResponseEntity<Proveedores> getProveedores(@PathVariable Long id) {
        log.debug("REST request to get Proveedores : {}", id);
        Optional<Proveedores> proveedores = proveedoresRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(proveedores);
    }

    /**
     * {@code DELETE  /proveedores/:id} : delete the "id" proveedores.
     *
     * @param id the id of the proveedores to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proveedores/{id}")
    public ResponseEntity<Void> deleteProveedores(@PathVariable Long id) {
        log.debug("REST request to delete Proveedores : {}", id);
        proveedoresRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
