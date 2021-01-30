package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.LacampesinaApp;
import com.mkp.lacampesina.domain.Precios;
import com.mkp.lacampesina.repository.PreciosRepository;

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
 * Integration tests for the {@link PreciosResource} REST controller.
 */
@SpringBootTest(classes = LacampesinaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PreciosResourceIT {

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_PRECIO = 1L;
    private static final Long UPDATED_PRECIO = 2L;

    @Autowired
    private PreciosRepository preciosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPreciosMockMvc;

    private Precios precios;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Precios createEntity(EntityManager em) {
        Precios precios = new Precios()
            .createdAt(DEFAULT_CREATED_AT)
            .fechaFin(DEFAULT_FECHA_FIN)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .precio(DEFAULT_PRECIO);
        return precios;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Precios createUpdatedEntity(EntityManager em) {
        Precios precios = new Precios()
            .createdAt(UPDATED_CREATED_AT)
            .fechaFin(UPDATED_FECHA_FIN)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .precio(UPDATED_PRECIO);
        return precios;
    }

    @BeforeEach
    public void initTest() {
        precios = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrecios() throws Exception {
        int databaseSizeBeforeCreate = preciosRepository.findAll().size();
        // Create the Precios
        restPreciosMockMvc.perform(post("/api/precios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(precios)))
            .andExpect(status().isCreated());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeCreate + 1);
        Precios testPrecios = preciosList.get(preciosList.size() - 1);
        assertThat(testPrecios.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPrecios.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testPrecios.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testPrecios.getPrecio()).isEqualTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    public void createPreciosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = preciosRepository.findAll().size();

        // Create the Precios with an existing ID
        precios.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreciosMockMvc.perform(post("/api/precios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(precios)))
            .andExpect(status().isBadRequest());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPrecios() throws Exception {
        // Initialize the database
        preciosRepository.saveAndFlush(precios);

        // Get all the preciosList
        restPreciosMockMvc.perform(get("/api/precios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(precios.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.intValue())));
    }
    
    @Test
    @Transactional
    public void getPrecios() throws Exception {
        // Initialize the database
        preciosRepository.saveAndFlush(precios);

        // Get the precios
        restPreciosMockMvc.perform(get("/api/precios/{id}", precios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(precios.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPrecios() throws Exception {
        // Get the precios
        restPreciosMockMvc.perform(get("/api/precios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrecios() throws Exception {
        // Initialize the database
        preciosRepository.saveAndFlush(precios);

        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();

        // Update the precios
        Precios updatedPrecios = preciosRepository.findById(precios.getId()).get();
        // Disconnect from session so that the updates on updatedPrecios are not directly saved in db
        em.detach(updatedPrecios);
        updatedPrecios
            .createdAt(UPDATED_CREATED_AT)
            .fechaFin(UPDATED_FECHA_FIN)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .precio(UPDATED_PRECIO);

        restPreciosMockMvc.perform(put("/api/precios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrecios)))
            .andExpect(status().isOk());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
        Precios testPrecios = preciosList.get(preciosList.size() - 1);
        assertThat(testPrecios.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPrecios.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testPrecios.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testPrecios.getPrecio()).isEqualTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    public void updateNonExistingPrecios() throws Exception {
        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreciosMockMvc.perform(put("/api/precios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(precios)))
            .andExpect(status().isBadRequest());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePrecios() throws Exception {
        // Initialize the database
        preciosRepository.saveAndFlush(precios);

        int databaseSizeBeforeDelete = preciosRepository.findAll().size();

        // Delete the precios
        restPreciosMockMvc.perform(delete("/api/precios/{id}", precios.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
