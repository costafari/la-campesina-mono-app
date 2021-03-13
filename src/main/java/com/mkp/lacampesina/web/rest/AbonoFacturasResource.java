package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.domain.AbonoFacturas;
import com.mkp.lacampesina.repository.AbonoFacturasRepository;
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
 * REST controller for managing {@link com.mkp.lacampesina.domain.AbonoFacturas}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AbonoFacturasResource {
    private final Logger log = LoggerFactory.getLogger(AbonoFacturasResource.class);

    private static final String ENTITY_NAME = "abonoFacturas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AbonoFacturasRepository abonoFacturasRepository;

    public AbonoFacturasResource(AbonoFacturasRepository abonoFacturasRepository) {
        this.abonoFacturasRepository = abonoFacturasRepository;
    }

    /**
     * {@code POST  /abono-facturas} : Create a new abonoFacturas.
     *
     * @param abonoFacturas the abonoFacturas to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new abonoFacturas, or with status {@code 400 (Bad Request)} if the abonoFacturas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/abono-facturas")
    public ResponseEntity<AbonoFacturas> createAbonoFacturas(@Valid @RequestBody AbonoFacturas abonoFacturas) throws URISyntaxException {
        log.debug("REST request to save AbonoFacturas : {}", abonoFacturas);
        if (abonoFacturas.getId() != null) {
            throw new BadRequestAlertException("A new abonoFacturas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AbonoFacturas result = abonoFacturasRepository.save(abonoFacturas);
        return ResponseEntity
            .created(new URI("/api/abono-facturas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /abono-facturas} : Updates an existing abonoFacturas.
     *
     * @param abonoFacturas the abonoFacturas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated abonoFacturas,
     * or with status {@code 400 (Bad Request)} if the abonoFacturas is not valid,
     * or with status {@code 500 (Internal Server Error)} if the abonoFacturas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/abono-facturas")
    public ResponseEntity<AbonoFacturas> updateAbonoFacturas(@Valid @RequestBody AbonoFacturas abonoFacturas) throws URISyntaxException {
        log.debug("REST request to update AbonoFacturas : {}", abonoFacturas);
        if (abonoFacturas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AbonoFacturas result = abonoFacturasRepository.save(abonoFacturas);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, abonoFacturas.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /abono-facturas} : get all the abonoFacturas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of abonoFacturas in body.
     */
    @GetMapping("/abono-facturas")
    public List<AbonoFacturas> getAllAbonoFacturas() {
        log.debug("REST request to get all AbonoFacturas");
        return abonoFacturasRepository.findAll();
    }

    /**
     * {@code GET  /abono-facturas/:id} : get the "id" abonoFacturas.
     *
     * @param id the id of the abonoFacturas to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the abonoFacturas, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/abono-facturas/{id}")
    public ResponseEntity<AbonoFacturas> getAbonoFacturas(@PathVariable Long id) {
        log.debug("REST request to get AbonoFacturas : {}", id);
        Optional<AbonoFacturas> abonoFacturas = abonoFacturasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(abonoFacturas);
    }

    /**
     * {@code DELETE  /abono-facturas/:id} : delete the "id" abonoFacturas.
     *
     * @param id the id of the abonoFacturas to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/abono-facturas/{id}")
    public ResponseEntity<Void> deleteAbonoFacturas(@PathVariable Long id) {
        log.debug("REST request to delete AbonoFacturas : {}", id);
        abonoFacturasRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
