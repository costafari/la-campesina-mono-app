package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.LacampesinaApp;
import com.mkp.lacampesina.domain.Tickets;
import com.mkp.lacampesina.repository.TicketsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TicketsResource} REST controller.
 */
@SpringBootTest(classes = LacampesinaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TicketsResourceIT {

    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;

    private static final LocalDate DEFAULT_FECHA_EXPEDICION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_EXPEDICION = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_TOTAL = 1L;
    private static final Long UPDATED_TOTAL = 2L;

    @Autowired
    private TicketsRepository ticketsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketsMockMvc;

    private Tickets tickets;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tickets createEntity(EntityManager em) {
        Tickets tickets = new Tickets()
            .cantidad(DEFAULT_CANTIDAD)
            .fechaExpedicion(DEFAULT_FECHA_EXPEDICION)
            .total(DEFAULT_TOTAL);
        return tickets;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tickets createUpdatedEntity(EntityManager em) {
        Tickets tickets = new Tickets()
            .cantidad(UPDATED_CANTIDAD)
            .fechaExpedicion(UPDATED_FECHA_EXPEDICION)
            .total(UPDATED_TOTAL);
        return tickets;
    }

    @BeforeEach
    public void initTest() {
        tickets = createEntity(em);
    }

    @Test
    @Transactional
    public void createTickets() throws Exception {
        int databaseSizeBeforeCreate = ticketsRepository.findAll().size();
        // Create the Tickets
        restTicketsMockMvc.perform(post("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tickets)))
            .andExpect(status().isCreated());

        // Validate the Tickets in the database
        List<Tickets> ticketsList = ticketsRepository.findAll();
        assertThat(ticketsList).hasSize(databaseSizeBeforeCreate + 1);
        Tickets testTickets = ticketsList.get(ticketsList.size() - 1);
        assertThat(testTickets.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testTickets.getFechaExpedicion()).isEqualTo(DEFAULT_FECHA_EXPEDICION);
        assertThat(testTickets.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    public void createTicketsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ticketsRepository.findAll().size();

        // Create the Tickets with an existing ID
        tickets.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketsMockMvc.perform(post("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tickets)))
            .andExpect(status().isBadRequest());

        // Validate the Tickets in the database
        List<Tickets> ticketsList = ticketsRepository.findAll();
        assertThat(ticketsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTickets() throws Exception {
        // Initialize the database
        ticketsRepository.saveAndFlush(tickets);

        // Get all the ticketsList
        restTicketsMockMvc.perform(get("/api/tickets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tickets.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
            .andExpect(jsonPath("$.[*].fechaExpedicion").value(hasItem(DEFAULT_FECHA_EXPEDICION.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }
    
    @Test
    @Transactional
    public void getTickets() throws Exception {
        // Initialize the database
        ticketsRepository.saveAndFlush(tickets);

        // Get the tickets
        restTicketsMockMvc.perform(get("/api/tickets/{id}", tickets.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tickets.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.intValue()))
            .andExpect(jsonPath("$.fechaExpedicion").value(DEFAULT_FECHA_EXPEDICION.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingTickets() throws Exception {
        // Get the tickets
        restTicketsMockMvc.perform(get("/api/tickets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTickets() throws Exception {
        // Initialize the database
        ticketsRepository.saveAndFlush(tickets);

        int databaseSizeBeforeUpdate = ticketsRepository.findAll().size();

        // Update the tickets
        Tickets updatedTickets = ticketsRepository.findById(tickets.getId()).get();
        // Disconnect from session so that the updates on updatedTickets are not directly saved in db
        em.detach(updatedTickets);
        updatedTickets
            .cantidad(UPDATED_CANTIDAD)
            .fechaExpedicion(UPDATED_FECHA_EXPEDICION)
            .total(UPDATED_TOTAL);

        restTicketsMockMvc.perform(put("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTickets)))
            .andExpect(status().isOk());

        // Validate the Tickets in the database
        List<Tickets> ticketsList = ticketsRepository.findAll();
        assertThat(ticketsList).hasSize(databaseSizeBeforeUpdate);
        Tickets testTickets = ticketsList.get(ticketsList.size() - 1);
        assertThat(testTickets.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testTickets.getFechaExpedicion()).isEqualTo(UPDATED_FECHA_EXPEDICION);
        assertThat(testTickets.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingTickets() throws Exception {
        int databaseSizeBeforeUpdate = ticketsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketsMockMvc.perform(put("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tickets)))
            .andExpect(status().isBadRequest());

        // Validate the Tickets in the database
        List<Tickets> ticketsList = ticketsRepository.findAll();
        assertThat(ticketsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTickets() throws Exception {
        // Initialize the database
        ticketsRepository.saveAndFlush(tickets);

        int databaseSizeBeforeDelete = ticketsRepository.findAll().size();

        // Delete the tickets
        restTicketsMockMvc.perform(delete("/api/tickets/{id}", tickets.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tickets> ticketsList = ticketsRepository.findAll();
        assertThat(ticketsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
