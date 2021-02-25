package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.LacampesinaApp;
import com.mkp.lacampesina.domain.Generalidades;
import com.mkp.lacampesina.repository.GeneralidadesRepository;

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
 * Integration tests for the {@link GeneralidadesResource} REST controller.
 */
@SpringBootTest(classes = LacampesinaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class GeneralidadesResourceIT {

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOMBRE_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_EMPRESA = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_PROPIETARIO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_PROPIETARIO = "BBBBBBBBBB";

    @Autowired
    private GeneralidadesRepository generalidadesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeneralidadesMockMvc;

    private Generalidades generalidades;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Generalidades createEntity(EntityManager em) {
        Generalidades generalidades = new Generalidades()
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .nombreEmpresa(DEFAULT_NOMBRE_EMPRESA)
            .nombrePropietario(DEFAULT_NOMBRE_PROPIETARIO);
        return generalidades;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Generalidades createUpdatedEntity(EntityManager em) {
        Generalidades generalidades = new Generalidades()
            .fechaInicio(UPDATED_FECHA_INICIO)
            .nombreEmpresa(UPDATED_NOMBRE_EMPRESA)
            .nombrePropietario(UPDATED_NOMBRE_PROPIETARIO);
        return generalidades;
    }

    @BeforeEach
    public void initTest() {
        generalidades = createEntity(em);
    }

    @Test
    @Transactional
    public void createGeneralidades() throws Exception {
        int databaseSizeBeforeCreate = generalidadesRepository.findAll().size();
        // Create the Generalidades
        restGeneralidadesMockMvc.perform(post("/api/generalidades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(generalidades)))
            .andExpect(status().isCreated());

        // Validate the Generalidades in the database
        List<Generalidades> generalidadesList = generalidadesRepository.findAll();
        assertThat(generalidadesList).hasSize(databaseSizeBeforeCreate + 1);
        Generalidades testGeneralidades = generalidadesList.get(generalidadesList.size() - 1);
        assertThat(testGeneralidades.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testGeneralidades.getNombreEmpresa()).isEqualTo(DEFAULT_NOMBRE_EMPRESA);
        assertThat(testGeneralidades.getNombrePropietario()).isEqualTo(DEFAULT_NOMBRE_PROPIETARIO);
    }

    @Test
    @Transactional
    public void createGeneralidadesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = generalidadesRepository.findAll().size();

        // Create the Generalidades with an existing ID
        generalidades.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneralidadesMockMvc.perform(post("/api/generalidades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(generalidades)))
            .andExpect(status().isBadRequest());

        // Validate the Generalidades in the database
        List<Generalidades> generalidadesList = generalidadesRepository.findAll();
        assertThat(generalidadesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllGeneralidades() throws Exception {
        // Initialize the database
        generalidadesRepository.saveAndFlush(generalidades);

        // Get all the generalidadesList
        restGeneralidadesMockMvc.perform(get("/api/generalidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generalidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].nombreEmpresa").value(hasItem(DEFAULT_NOMBRE_EMPRESA)))
            .andExpect(jsonPath("$.[*].nombrePropietario").value(hasItem(DEFAULT_NOMBRE_PROPIETARIO)));
    }
    
    @Test
    @Transactional
    public void getGeneralidades() throws Exception {
        // Initialize the database
        generalidadesRepository.saveAndFlush(generalidades);

        // Get the generalidades
        restGeneralidadesMockMvc.perform(get("/api/generalidades/{id}", generalidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(generalidades.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.nombreEmpresa").value(DEFAULT_NOMBRE_EMPRESA))
            .andExpect(jsonPath("$.nombrePropietario").value(DEFAULT_NOMBRE_PROPIETARIO));
    }
    @Test
    @Transactional
    public void getNonExistingGeneralidades() throws Exception {
        // Get the generalidades
        restGeneralidadesMockMvc.perform(get("/api/generalidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeneralidades() throws Exception {
        // Initialize the database
        generalidadesRepository.saveAndFlush(generalidades);

        int databaseSizeBeforeUpdate = generalidadesRepository.findAll().size();

        // Update the generalidades
        Generalidades updatedGeneralidades = generalidadesRepository.findById(generalidades.getId()).get();
        // Disconnect from session so that the updates on updatedGeneralidades are not directly saved in db
        em.detach(updatedGeneralidades);
        updatedGeneralidades
            .fechaInicio(UPDATED_FECHA_INICIO)
            .nombreEmpresa(UPDATED_NOMBRE_EMPRESA)
            .nombrePropietario(UPDATED_NOMBRE_PROPIETARIO);

        restGeneralidadesMockMvc.perform(put("/api/generalidades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedGeneralidades)))
            .andExpect(status().isOk());

        // Validate the Generalidades in the database
        List<Generalidades> generalidadesList = generalidadesRepository.findAll();
        assertThat(generalidadesList).hasSize(databaseSizeBeforeUpdate);
        Generalidades testGeneralidades = generalidadesList.get(generalidadesList.size() - 1);
        assertThat(testGeneralidades.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testGeneralidades.getNombreEmpresa()).isEqualTo(UPDATED_NOMBRE_EMPRESA);
        assertThat(testGeneralidades.getNombrePropietario()).isEqualTo(UPDATED_NOMBRE_PROPIETARIO);
    }

    @Test
    @Transactional
    public void updateNonExistingGeneralidades() throws Exception {
        int databaseSizeBeforeUpdate = generalidadesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneralidadesMockMvc.perform(put("/api/generalidades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(generalidades)))
            .andExpect(status().isBadRequest());

        // Validate the Generalidades in the database
        List<Generalidades> generalidadesList = generalidadesRepository.findAll();
        assertThat(generalidadesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGeneralidades() throws Exception {
        // Initialize the database
        generalidadesRepository.saveAndFlush(generalidades);

        int databaseSizeBeforeDelete = generalidadesRepository.findAll().size();

        // Delete the generalidades
        restGeneralidadesMockMvc.perform(delete("/api/generalidades/{id}", generalidades.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Generalidades> generalidadesList = generalidadesRepository.findAll();
        assertThat(generalidadesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
