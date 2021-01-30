package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.LacampesinaApp;
import com.mkp.lacampesina.domain.Lotes;
import com.mkp.lacampesina.repository.LotesRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LotesResource} REST controller.
 */
@SpringBootTest(classes = LacampesinaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LotesResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_ENTRADA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_ENTRADA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOTE = "AAAAAAAAAA";
    private static final String UPDATED_LOTE = "BBBBBBBBBB";

    @Autowired
    private LotesRepository lotesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLotesMockMvc;

    private Lotes lotes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lotes createEntity(EntityManager em) {
        Lotes lotes = new Lotes()
            .cantidad(DEFAULT_CANTIDAD)
            .createdAt(DEFAULT_CREATED_AT)
            .fechaEntrada(DEFAULT_FECHA_ENTRADA)
            .lote(DEFAULT_LOTE);
        return lotes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lotes createUpdatedEntity(EntityManager em) {
        Lotes lotes = new Lotes()
            .cantidad(UPDATED_CANTIDAD)
            .createdAt(UPDATED_CREATED_AT)
            .fechaEntrada(UPDATED_FECHA_ENTRADA)
            .lote(UPDATED_LOTE);
        return lotes;
    }

    @BeforeEach
    public void initTest() {
        lotes = createEntity(em);
    }

    @Test
    @Transactional
    public void createLotes() throws Exception {
        int databaseSizeBeforeCreate = lotesRepository.findAll().size();
        // Create the Lotes
        restLotesMockMvc.perform(post("/api/lotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lotes)))
            .andExpect(status().isCreated());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeCreate + 1);
        Lotes testLotes = lotesList.get(lotesList.size() - 1);
        assertThat(testLotes.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testLotes.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testLotes.getFechaEntrada()).isEqualTo(DEFAULT_FECHA_ENTRADA);
        assertThat(testLotes.getLote()).isEqualTo(DEFAULT_LOTE);
    }

    @Test
    @Transactional
    public void createLotesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lotesRepository.findAll().size();

        // Create the Lotes with an existing ID
        lotes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLotesMockMvc.perform(post("/api/lotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lotes)))
            .andExpect(status().isBadRequest());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLotes() throws Exception {
        // Initialize the database
        lotesRepository.saveAndFlush(lotes);

        // Get all the lotesList
        restLotesMockMvc.perform(get("/api/lotes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lotes.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].fechaEntrada").value(hasItem(DEFAULT_FECHA_ENTRADA.toString())))
            .andExpect(jsonPath("$.[*].lote").value(hasItem(DEFAULT_LOTE)));
    }
    
    @Test
    @Transactional
    public void getLotes() throws Exception {
        // Initialize the database
        lotesRepository.saveAndFlush(lotes);

        // Get the lotes
        restLotesMockMvc.perform(get("/api/lotes/{id}", lotes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lotes.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.fechaEntrada").value(DEFAULT_FECHA_ENTRADA.toString()))
            .andExpect(jsonPath("$.lote").value(DEFAULT_LOTE));
    }
    @Test
    @Transactional
    public void getNonExistingLotes() throws Exception {
        // Get the lotes
        restLotesMockMvc.perform(get("/api/lotes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLotes() throws Exception {
        // Initialize the database
        lotesRepository.saveAndFlush(lotes);

        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();

        // Update the lotes
        Lotes updatedLotes = lotesRepository.findById(lotes.getId()).get();
        // Disconnect from session so that the updates on updatedLotes are not directly saved in db
        em.detach(updatedLotes);
        updatedLotes
            .cantidad(UPDATED_CANTIDAD)
            .createdAt(UPDATED_CREATED_AT)
            .fechaEntrada(UPDATED_FECHA_ENTRADA)
            .lote(UPDATED_LOTE);

        restLotesMockMvc.perform(put("/api/lotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLotes)))
            .andExpect(status().isOk());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
        Lotes testLotes = lotesList.get(lotesList.size() - 1);
        assertThat(testLotes.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testLotes.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testLotes.getFechaEntrada()).isEqualTo(UPDATED_FECHA_ENTRADA);
        assertThat(testLotes.getLote()).isEqualTo(UPDATED_LOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingLotes() throws Exception {
        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotesMockMvc.perform(put("/api/lotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lotes)))
            .andExpect(status().isBadRequest());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLotes() throws Exception {
        // Initialize the database
        lotesRepository.saveAndFlush(lotes);

        int databaseSizeBeforeDelete = lotesRepository.findAll().size();

        // Delete the lotes
        restLotesMockMvc.perform(delete("/api/lotes/{id}", lotes.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
