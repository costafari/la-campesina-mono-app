package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.domain.Tickets;
import com.mkp.lacampesina.repository.TicketsRepository;
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
 * REST controller for managing {@link com.mkp.lacampesina.domain.Tickets}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TicketsResource {

    private final Logger log = LoggerFactory.getLogger(TicketsResource.class);

    private static final String ENTITY_NAME = "tickets";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketsRepository ticketsRepository;

    public TicketsResource(TicketsRepository ticketsRepository) {
        this.ticketsRepository = ticketsRepository;
    }

    /**
     * {@code POST  /tickets} : Create a new tickets.
     *
     * @param tickets the tickets to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tickets, or with status {@code 400 (Bad Request)} if the tickets has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tickets")
    public ResponseEntity<Tickets> createTickets(@RequestBody Tickets tickets) throws URISyntaxException {
        log.debug("REST request to save Tickets : {}", tickets);
        if (tickets.getId() != null) {
            throw new BadRequestAlertException("A new tickets cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tickets result = ticketsRepository.save(tickets);
        return ResponseEntity.created(new URI("/api/tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tickets} : Updates an existing tickets.
     *
     * @param tickets the tickets to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickets,
     * or with status {@code 400 (Bad Request)} if the tickets is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tickets couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tickets")
    public ResponseEntity<Tickets> updateTickets(@RequestBody Tickets tickets) throws URISyntaxException {
        log.debug("REST request to update Tickets : {}", tickets);
        if (tickets.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tickets result = ticketsRepository.save(tickets);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tickets.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tickets} : get all the tickets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickets in body.
     */
    @GetMapping("/tickets")
    public List<Tickets> getAllTickets() {
        log.debug("REST request to get all Tickets");
        return ticketsRepository.findAll();
    }

    /**
     * {@code GET  /tickets/:id} : get the "id" tickets.
     *
     * @param id the id of the tickets to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tickets, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets/{id}")
    public ResponseEntity<Tickets> getTickets(@PathVariable Long id) {
        log.debug("REST request to get Tickets : {}", id);
        Optional<Tickets> tickets = ticketsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tickets);
    }

    /**
     * {@code DELETE  /tickets/:id} : delete the "id" tickets.
     *
     * @param id the id of the tickets to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> deleteTickets(@PathVariable Long id) {
        log.debug("REST request to delete Tickets : {}", id);
        ticketsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
