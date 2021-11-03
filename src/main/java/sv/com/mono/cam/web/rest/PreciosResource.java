package sv.com.mono.cam.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sv.com.mono.cam.domain.Precios;
import sv.com.mono.cam.repository.PreciosRepository;
import sv.com.mono.cam.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sv.com.mono.cam.domain.Precios}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PreciosResource {

    private final Logger log = LoggerFactory.getLogger(PreciosResource.class);

    private static final String ENTITY_NAME = "precios";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PreciosRepository preciosRepository;

    public PreciosResource(PreciosRepository preciosRepository) {
        this.preciosRepository = preciosRepository;
    }

    /**
     * {@code POST  /precios} : Create a new precios.
     *
     * @param precios the precios to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new precios, or with status {@code 400 (Bad Request)} if the precios has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/precios")
    public ResponseEntity<Precios> createPrecios(@RequestBody Precios precios) throws URISyntaxException {
        log.debug("REST request to save Precios : {}", precios);
        if (precios.getId() != null) {
            throw new BadRequestAlertException("A new precios cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Precios result = preciosRepository.save(precios);
        return ResponseEntity
            .created(new URI("/api/precios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /precios/:id} : Updates an existing precios.
     *
     * @param id the id of the precios to save.
     * @param precios the precios to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated precios,
     * or with status {@code 400 (Bad Request)} if the precios is not valid,
     * or with status {@code 500 (Internal Server Error)} if the precios couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/precios/{id}")
    public ResponseEntity<Precios> updatePrecios(@PathVariable(value = "id", required = false) final Long id, @RequestBody Precios precios)
        throws URISyntaxException {
        log.debug("REST request to update Precios : {}, {}", id, precios);
        if (precios.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, precios.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!preciosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Precios result = preciosRepository.save(precios);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, precios.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /precios/:id} : Partial updates given fields of an existing precios, field will ignore if it is null
     *
     * @param id the id of the precios to save.
     * @param precios the precios to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated precios,
     * or with status {@code 400 (Bad Request)} if the precios is not valid,
     * or with status {@code 404 (Not Found)} if the precios is not found,
     * or with status {@code 500 (Internal Server Error)} if the precios couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/precios/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Precios> partialUpdatePrecios(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Precios precios
    ) throws URISyntaxException {
        log.debug("REST request to partial update Precios partially : {}, {}", id, precios);
        if (precios.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, precios.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!preciosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Precios> result = preciosRepository
            .findById(precios.getId())
            .map(
                existingPrecios -> {
                    if (precios.getFechaFin() != null) {
                        existingPrecios.setFechaFin(precios.getFechaFin());
                    }
                    if (precios.getFechaInicio() != null) {
                        existingPrecios.setFechaInicio(precios.getFechaInicio());
                    }
                    if (precios.getPrecio() != null) {
                        existingPrecios.setPrecio(precios.getPrecio());
                    }

                    return existingPrecios;
                }
            )
            .map(preciosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, precios.getId().toString())
        );
    }

    /**
     * {@code GET  /precios} : get all the precios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of precios in body.
     */
    @GetMapping("/precios")
    public List<Precios> getAllPrecios() {
        log.debug("REST request to get all Precios");
        return preciosRepository.findAll();
    }

    /**
     * {@code GET  /precios/:id} : get the "id" precios.
     *
     * @param id the id of the precios to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the precios, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/precios/{id}")
    public ResponseEntity<Precios> getPrecios(@PathVariable Long id) {
        log.debug("REST request to get Precios : {}", id);
        Optional<Precios> precios = preciosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(precios);
    }

    /**
     * {@code DELETE  /precios/:id} : delete the "id" precios.
     *
     * @param id the id of the precios to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/precios/{id}")
    public ResponseEntity<Void> deletePrecios(@PathVariable Long id) {
        log.debug("REST request to delete Precios : {}", id);
        preciosRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
