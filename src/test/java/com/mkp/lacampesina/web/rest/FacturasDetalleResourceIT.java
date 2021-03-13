package com.mkp.lacampesina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mkp.lacampesina.LacampesinaApp;
import com.mkp.lacampesina.domain.FacturasDetalle;
import com.mkp.lacampesina.repository.FacturasDetalleRepository;
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
 * Integration tests for the {@link FacturasDetalleResource} REST controller.
 */
@SpringBootTest(classes = LacampesinaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FacturasDetalleResourceIT {
    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;

    private static final Long DEFAULT_TOTAL = 1L;
    private static final Long UPDATED_TOTAL = 2L;

    @Autowired
    private FacturasDetalleRepository facturasDetalleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturasDetalleMockMvc;

    private FacturasDetalle facturasDetalle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacturasDetalle createEntity(EntityManager em) {
        FacturasDetalle facturasDetalle = new FacturasDetalle().cantidad(DEFAULT_CANTIDAD).total(DEFAULT_TOTAL);
        return facturasDetalle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacturasDetalle createUpdatedEntity(EntityManager em) {
        FacturasDetalle facturasDetalle = new FacturasDetalle().cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);
        return facturasDetalle;
    }

    @BeforeEach
    public void initTest() {
        facturasDetalle = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacturasDetalle() throws Exception {
        int databaseSizeBeforeCreate = facturasDetalleRepository.findAll().size();
        // Create the FacturasDetalle
        restFacturasDetalleMockMvc
            .perform(
                post("/api/facturas-detalles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturasDetalle))
            )
            .andExpect(status().isCreated());

        // Validate the FacturasDetalle in the database
        List<FacturasDetalle> facturasDetalleList = facturasDetalleRepository.findAll();
        assertThat(facturasDetalleList).hasSize(databaseSizeBeforeCreate + 1);
        FacturasDetalle testFacturasDetalle = facturasDetalleList.get(facturasDetalleList.size() - 1);
        assertThat(testFacturasDetalle.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testFacturasDetalle.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    public void createFacturasDetalleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facturasDetalleRepository.findAll().size();

        // Create the FacturasDetalle with an existing ID
        facturasDetalle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturasDetalleMockMvc
            .perform(
                post("/api/facturas-detalles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturasDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturasDetalle in the database
        List<FacturasDetalle> facturasDetalleList = facturasDetalleRepository.findAll();
        assertThat(facturasDetalleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasDetalleRepository.findAll().size();
        // set the field null
        facturasDetalle.setCantidad(null);

        // Create the FacturasDetalle, which fails.

        restFacturasDetalleMockMvc
            .perform(
                post("/api/facturas-detalles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturasDetalle))
            )
            .andExpect(status().isBadRequest());

        List<FacturasDetalle> facturasDetalleList = facturasDetalleRepository.findAll();
        assertThat(facturasDetalleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFacturasDetalles() throws Exception {
        // Initialize the database
        facturasDetalleRepository.saveAndFlush(facturasDetalle);

        // Get all the facturasDetalleList
        restFacturasDetalleMockMvc
            .perform(get("/api/facturas-detalles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturasDetalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }

    @Test
    @Transactional
    public void getFacturasDetalle() throws Exception {
        // Initialize the database
        facturasDetalleRepository.saveAndFlush(facturasDetalle);

        // Get the facturasDetalle
        restFacturasDetalleMockMvc
            .perform(get("/api/facturas-detalles/{id}", facturasDetalle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facturasDetalle.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFacturasDetalle() throws Exception {
        // Get the facturasDetalle
        restFacturasDetalleMockMvc.perform(get("/api/facturas-detalles/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacturasDetalle() throws Exception {
        // Initialize the database
        facturasDetalleRepository.saveAndFlush(facturasDetalle);

        int databaseSizeBeforeUpdate = facturasDetalleRepository.findAll().size();

        // Update the facturasDetalle
        FacturasDetalle updatedFacturasDetalle = facturasDetalleRepository.findById(facturasDetalle.getId()).get();
        // Disconnect from session so that the updates on updatedFacturasDetalle are not directly saved in db
        em.detach(updatedFacturasDetalle);
        updatedFacturasDetalle.cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);

        restFacturasDetalleMockMvc
            .perform(
                put("/api/facturas-detalles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFacturasDetalle))
            )
            .andExpect(status().isOk());

        // Validate the FacturasDetalle in the database
        List<FacturasDetalle> facturasDetalleList = facturasDetalleRepository.findAll();
        assertThat(facturasDetalleList).hasSize(databaseSizeBeforeUpdate);
        FacturasDetalle testFacturasDetalle = facturasDetalleList.get(facturasDetalleList.size() - 1);
        assertThat(testFacturasDetalle.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testFacturasDetalle.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingFacturasDetalle() throws Exception {
        int databaseSizeBeforeUpdate = facturasDetalleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturasDetalleMockMvc
            .perform(
                put("/api/facturas-detalles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturasDetalle))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturasDetalle in the database
        List<FacturasDetalle> facturasDetalleList = facturasDetalleRepository.findAll();
        assertThat(facturasDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFacturasDetalle() throws Exception {
        // Initialize the database
        facturasDetalleRepository.saveAndFlush(facturasDetalle);

        int databaseSizeBeforeDelete = facturasDetalleRepository.findAll().size();

        // Delete the facturasDetalle
        restFacturasDetalleMockMvc
            .perform(delete("/api/facturas-detalles/{id}", facturasDetalle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FacturasDetalle> facturasDetalleList = facturasDetalleRepository.findAll();
        assertThat(facturasDetalleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
