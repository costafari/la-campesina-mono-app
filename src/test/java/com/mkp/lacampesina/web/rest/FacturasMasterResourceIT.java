package com.mkp.lacampesina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mkp.lacampesina.LacampesinaApp;
import com.mkp.lacampesina.domain.FacturasMaster;
import com.mkp.lacampesina.repository.FacturasMasterRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FacturasMasterResource} REST controller.
 */
@SpringBootTest(classes = LacampesinaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FacturasMasterResourceIT {
    private static final Long DEFAULT_NUMERO_FACTURA = 1L;
    private static final Long UPDATED_NUMERO_FACTURA = 2L;

    private static final Instant DEFAULT_FECHA_FACTURA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FACTURA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_CONDICION_PAGO = false;
    private static final Boolean UPDATED_CONDICION_PAGO = true;

    @Autowired
    private FacturasMasterRepository facturasMasterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturasMasterMockMvc;

    private FacturasMaster facturasMaster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacturasMaster createEntity(EntityManager em) {
        FacturasMaster facturasMaster = new FacturasMaster()
            .numeroFactura(DEFAULT_NUMERO_FACTURA)
            .fechaFactura(DEFAULT_FECHA_FACTURA)
            .condicionPago(DEFAULT_CONDICION_PAGO);
        return facturasMaster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacturasMaster createUpdatedEntity(EntityManager em) {
        FacturasMaster facturasMaster = new FacturasMaster()
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaFactura(UPDATED_FECHA_FACTURA)
            .condicionPago(UPDATED_CONDICION_PAGO);
        return facturasMaster;
    }

    @BeforeEach
    public void initTest() {
        facturasMaster = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacturasMaster() throws Exception {
        int databaseSizeBeforeCreate = facturasMasterRepository.findAll().size();
        // Create the FacturasMaster
        restFacturasMasterMockMvc
            .perform(
                post("/api/facturas-masters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturasMaster))
            )
            .andExpect(status().isCreated());

        // Validate the FacturasMaster in the database
        List<FacturasMaster> facturasMasterList = facturasMasterRepository.findAll();
        assertThat(facturasMasterList).hasSize(databaseSizeBeforeCreate + 1);
        FacturasMaster testFacturasMaster = facturasMasterList.get(facturasMasterList.size() - 1);
        assertThat(testFacturasMaster.getNumeroFactura()).isEqualTo(DEFAULT_NUMERO_FACTURA);
        assertThat(testFacturasMaster.getFechaFactura()).isEqualTo(DEFAULT_FECHA_FACTURA);
        assertThat(testFacturasMaster.isCondicionPago()).isEqualTo(DEFAULT_CONDICION_PAGO);
    }

    @Test
    @Transactional
    public void createFacturasMasterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facturasMasterRepository.findAll().size();

        // Create the FacturasMaster with an existing ID
        facturasMaster.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturasMasterMockMvc
            .perform(
                post("/api/facturas-masters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturasMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturasMaster in the database
        List<FacturasMaster> facturasMasterList = facturasMasterRepository.findAll();
        assertThat(facturasMasterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumeroFacturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasMasterRepository.findAll().size();
        // set the field null
        facturasMaster.setNumeroFactura(null);

        // Create the FacturasMaster, which fails.

        restFacturasMasterMockMvc
            .perform(
                post("/api/facturas-masters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturasMaster))
            )
            .andExpect(status().isBadRequest());

        List<FacturasMaster> facturasMasterList = facturasMasterRepository.findAll();
        assertThat(facturasMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFacturasMasters() throws Exception {
        // Initialize the database
        facturasMasterRepository.saveAndFlush(facturasMaster);

        // Get all the facturasMasterList
        restFacturasMasterMockMvc
            .perform(get("/api/facturas-masters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturasMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroFactura").value(hasItem(DEFAULT_NUMERO_FACTURA.intValue())))
            .andExpect(jsonPath("$.[*].fechaFactura").value(hasItem(DEFAULT_FECHA_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].condicionPago").value(hasItem(DEFAULT_CONDICION_PAGO.booleanValue())));
    }

    @Test
    @Transactional
    public void getFacturasMaster() throws Exception {
        // Initialize the database
        facturasMasterRepository.saveAndFlush(facturasMaster);

        // Get the facturasMaster
        restFacturasMasterMockMvc
            .perform(get("/api/facturas-masters/{id}", facturasMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facturasMaster.getId().intValue()))
            .andExpect(jsonPath("$.numeroFactura").value(DEFAULT_NUMERO_FACTURA.intValue()))
            .andExpect(jsonPath("$.fechaFactura").value(DEFAULT_FECHA_FACTURA.toString()))
            .andExpect(jsonPath("$.condicionPago").value(DEFAULT_CONDICION_PAGO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFacturasMaster() throws Exception {
        // Get the facturasMaster
        restFacturasMasterMockMvc.perform(get("/api/facturas-masters/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacturasMaster() throws Exception {
        // Initialize the database
        facturasMasterRepository.saveAndFlush(facturasMaster);

        int databaseSizeBeforeUpdate = facturasMasterRepository.findAll().size();

        // Update the facturasMaster
        FacturasMaster updatedFacturasMaster = facturasMasterRepository.findById(facturasMaster.getId()).get();
        // Disconnect from session so that the updates on updatedFacturasMaster are not directly saved in db
        em.detach(updatedFacturasMaster);
        updatedFacturasMaster
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaFactura(UPDATED_FECHA_FACTURA)
            .condicionPago(UPDATED_CONDICION_PAGO);

        restFacturasMasterMockMvc
            .perform(
                put("/api/facturas-masters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFacturasMaster))
            )
            .andExpect(status().isOk());

        // Validate the FacturasMaster in the database
        List<FacturasMaster> facturasMasterList = facturasMasterRepository.findAll();
        assertThat(facturasMasterList).hasSize(databaseSizeBeforeUpdate);
        FacturasMaster testFacturasMaster = facturasMasterList.get(facturasMasterList.size() - 1);
        assertThat(testFacturasMaster.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFacturasMaster.getFechaFactura()).isEqualTo(UPDATED_FECHA_FACTURA);
        assertThat(testFacturasMaster.isCondicionPago()).isEqualTo(UPDATED_CONDICION_PAGO);
    }

    @Test
    @Transactional
    public void updateNonExistingFacturasMaster() throws Exception {
        int databaseSizeBeforeUpdate = facturasMasterRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturasMasterMockMvc
            .perform(
                put("/api/facturas-masters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturasMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturasMaster in the database
        List<FacturasMaster> facturasMasterList = facturasMasterRepository.findAll();
        assertThat(facturasMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFacturasMaster() throws Exception {
        // Initialize the database
        facturasMasterRepository.saveAndFlush(facturasMaster);

        int databaseSizeBeforeDelete = facturasMasterRepository.findAll().size();

        // Delete the facturasMaster
        restFacturasMasterMockMvc
            .perform(delete("/api/facturas-masters/{id}", facturasMaster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FacturasMaster> facturasMasterList = facturasMasterRepository.findAll();
        assertThat(facturasMasterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
