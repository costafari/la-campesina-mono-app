package sv.com.mono.cam.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sv.com.mono.cam.domain.Lotes;
import sv.com.mono.cam.repository.LotesRepository;
import sv.com.mono.cam.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sv.com.mono.cam.domain.Lotes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LotesResource {

    private final Logger log = LoggerFactory.getLogger(LotesResource.class);

    private static final String ENTITY_NAME = "lotes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LotesRepository lotesRepository;

    public LotesResource(LotesRepository lotesRepository) {
        this.lotesRepository = lotesRepository;
    }

    /**
     * {@code POST  /lotes} : Create a new lotes.
     *
     * @param lotes the lotes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lotes, or with status {@code 400 (Bad Request)} if the lotes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lotes")
    public ResponseEntity<Lotes> createLotes(@RequestBody Lotes lotes) throws URISyntaxException {
        log.debug("REST request to save Lotes : {}", lotes);
        if (lotes.getId() != null) {
            throw new BadRequestAlertException("A new lotes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lotes result = lotesRepository.save(lotes);
        return ResponseEntity
            .created(new URI("/api/lotes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lotes/:id} : Updates an existing lotes.
     *
     * @param id the id of the lotes to save.
     * @param lotes the lotes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotes,
     * or with status {@code 400 (Bad Request)} if the lotes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lotes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lotes/{id}")
    public ResponseEntity<Lotes> updateLotes(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lotes lotes)
        throws URISyntaxException {
        log.debug("REST request to update Lotes : {}, {}", id, lotes);
        if (lotes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lotes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lotesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Lotes result = lotesRepository.save(lotes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lotes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lotes/:id} : Partial updates given fields of an existing lotes, field will ignore if it is null
     *
     * @param id the id of the lotes to save.
     * @param lotes the lotes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotes,
     * or with status {@code 400 (Bad Request)} if the lotes is not valid,
     * or with status {@code 404 (Not Found)} if the lotes is not found,
     * or with status {@code 500 (Internal Server Error)} if the lotes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lotes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Lotes> partialUpdateLotes(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lotes lotes)
        throws URISyntaxException {
        log.debug("REST request to partial update Lotes partially : {}, {}", id, lotes);
        if (lotes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lotes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lotesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lotes> result = lotesRepository
            .findById(lotes.getId())
            .map(
                existingLotes -> {
                    if (lotes.getCantidad() != null) {
                        existingLotes.setCantidad(lotes.getCantidad());
                    }
                    if (lotes.getFechaEntrada() != null) {
                        existingLotes.setFechaEntrada(lotes.getFechaEntrada());
                    }
                    if (lotes.getLote() != null) {
                        existingLotes.setLote(lotes.getLote());
                    }

                    return existingLotes;
                }
            )
            .map(lotesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lotes.getId().toString())
        );
    }

    /**
     * {@code GET  /lotes} : get all the lotes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lotes in body.
     */
    @GetMapping("/lotes")
    public List<Lotes> getAllLotes(@RequestParam(required = false) String filter) {
        if ("proveedores-is-null".equals(filter)) {
            log.debug("REST request to get all Lotess where proveedores is null");
            return StreamSupport
                .stream(lotesRepository.findAll().spliterator(), false)
                .filter(lotes -> lotes.getProveedores() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Lotes");
        return lotesRepository.findAll();
    }

    /**
     * {@code GET  /lotes/:id} : get the "id" lotes.
     *
     * @param id the id of the lotes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lotes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lotes/{id}")
    public ResponseEntity<Lotes> getLotes(@PathVariable Long id) {
        log.debug("REST request to get Lotes : {}", id);
        Optional<Lotes> lotes = lotesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lotes);
    }

    /**
     * {@code DELETE  /lotes/:id} : delete the "id" lotes.
     *
     * @param id the id of the lotes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lotes/{id}")
    public ResponseEntity<Void> deleteLotes(@PathVariable Long id) {
        log.debug("REST request to delete Lotes : {}", id);
        lotesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
