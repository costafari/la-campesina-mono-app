package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.domain.Generalidades;
import com.mkp.lacampesina.repository.GeneralidadesRepository;
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
 * REST controller for managing {@link com.mkp.lacampesina.domain.Generalidades}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GeneralidadesResource {

    private final Logger log = LoggerFactory.getLogger(GeneralidadesResource.class);

    private static final String ENTITY_NAME = "generalidades";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeneralidadesRepository generalidadesRepository;

    public GeneralidadesResource(GeneralidadesRepository generalidadesRepository) {
        this.generalidadesRepository = generalidadesRepository;
    }

    /**
     * {@code POST  /generalidades} : Create a new generalidades.
     *
     * @param generalidades the generalidades to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new generalidades, or with status {@code 400 (Bad Request)} if the generalidades has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/generalidades")
    public ResponseEntity<Generalidades> createGeneralidades(@RequestBody Generalidades generalidades) throws URISyntaxException {
        log.debug("REST request to save Generalidades : {}", generalidades);
        if (generalidades.getId() != null) {
            throw new BadRequestAlertException("A new generalidades cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Generalidades result = generalidadesRepository.save(generalidades);
        return ResponseEntity.created(new URI("/api/generalidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /generalidades} : Updates an existing generalidades.
     *
     * @param generalidades the generalidades to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generalidades,
     * or with status {@code 400 (Bad Request)} if the generalidades is not valid,
     * or with status {@code 500 (Internal Server Error)} if the generalidades couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/generalidades")
    public ResponseEntity<Generalidades> updateGeneralidades(@RequestBody Generalidades generalidades) throws URISyntaxException {
        log.debug("REST request to update Generalidades : {}", generalidades);
        if (generalidades.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Generalidades result = generalidadesRepository.save(generalidades);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, generalidades.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /generalidades} : get all the generalidades.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of generalidades in body.
     */
    @GetMapping("/generalidades")
    public List<Generalidades> getAllGeneralidades() {
        log.debug("REST request to get all Generalidades");
        return generalidadesRepository.findAll();
    }

    /**
     * {@code GET  /generalidades/:id} : get the "id" generalidades.
     *
     * @param id the id of the generalidades to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the generalidades, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/generalidades/{id}")
    public ResponseEntity<Generalidades> getGeneralidades(@PathVariable Long id) {
        log.debug("REST request to get Generalidades : {}", id);
        Optional<Generalidades> generalidades = generalidadesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(generalidades);
    }

    /**
     * {@code DELETE  /generalidades/:id} : delete the "id" generalidades.
     *
     * @param id the id of the generalidades to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/generalidades/{id}")
    public ResponseEntity<Void> deleteGeneralidades(@PathVariable Long id) {
        log.debug("REST request to delete Generalidades : {}", id);
        generalidadesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
