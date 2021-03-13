package com.mkp.lacampesina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mkp.lacampesina.LacampesinaApp;
import com.mkp.lacampesina.domain.AbonoFacturas;
import com.mkp.lacampesina.repository.AbonoFacturasRepository;
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
 * Integration tests for the {@link AbonoFacturasResource} REST controller.
 */
@SpringBootTest(classes = LacampesinaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AbonoFacturasResourceIT {
    private static final Long DEFAULT_SALDO_ANTERIOR = 1L;
    private static final Long UPDATED_SALDO_ANTERIOR = 2L;

    private static final Long DEFAULT_ABONO = 1L;
    private static final Long UPDATED_ABONO = 2L;

    private static final Long DEFAULT_NUEVO_SALDO = 1L;
    private static final Long UPDATED_NUEVO_SALDO = 2L;

    @Autowired
    private AbonoFacturasRepository abonoFacturasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAbonoFacturasMockMvc;

    private AbonoFacturas abonoFacturas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AbonoFacturas createEntity(EntityManager em) {
        AbonoFacturas abonoFacturas = new AbonoFacturas()
            .saldoAnterior(DEFAULT_SALDO_ANTERIOR)
            .abono(DEFAULT_ABONO)
            .nuevoSaldo(DEFAULT_NUEVO_SALDO);
        return abonoFacturas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AbonoFacturas createUpdatedEntity(EntityManager em) {
        AbonoFacturas abonoFacturas = new AbonoFacturas()
            .saldoAnterior(UPDATED_SALDO_ANTERIOR)
            .abono(UPDATED_ABONO)
            .nuevoSaldo(UPDATED_NUEVO_SALDO);
        return abonoFacturas;
    }

    @BeforeEach
    public void initTest() {
        abonoFacturas = createEntity(em);
    }

    @Test
    @Transactional
    public void createAbonoFacturas() throws Exception {
        int databaseSizeBeforeCreate = abonoFacturasRepository.findAll().size();
        // Create the AbonoFacturas
        restAbonoFacturasMockMvc
            .perform(
                post("/api/abono-facturas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonoFacturas))
            )
            .andExpect(status().isCreated());

        // Validate the AbonoFacturas in the database
        List<AbonoFacturas> abonoFacturasList = abonoFacturasRepository.findAll();
        assertThat(abonoFacturasList).hasSize(databaseSizeBeforeCreate + 1);
        AbonoFacturas testAbonoFacturas = abonoFacturasList.get(abonoFacturasList.size() - 1);
        assertThat(testAbonoFacturas.getSaldoAnterior()).isEqualTo(DEFAULT_SALDO_ANTERIOR);
        assertThat(testAbonoFacturas.getAbono()).isEqualTo(DEFAULT_ABONO);
        assertThat(testAbonoFacturas.getNuevoSaldo()).isEqualTo(DEFAULT_NUEVO_SALDO);
    }

    @Test
    @Transactional
    public void createAbonoFacturasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = abonoFacturasRepository.findAll().size();

        // Create the AbonoFacturas with an existing ID
        abonoFacturas.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbonoFacturasMockMvc
            .perform(
                post("/api/abono-facturas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonoFacturas))
            )
            .andExpect(status().isBadRequest());

        // Validate the AbonoFacturas in the database
        List<AbonoFacturas> abonoFacturasList = abonoFacturasRepository.findAll();
        assertThat(abonoFacturasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSaldoAnteriorIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonoFacturasRepository.findAll().size();
        // set the field null
        abonoFacturas.setSaldoAnterior(null);

        // Create the AbonoFacturas, which fails.

        restAbonoFacturasMockMvc
            .perform(
                post("/api/abono-facturas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonoFacturas))
            )
            .andExpect(status().isBadRequest());

        List<AbonoFacturas> abonoFacturasList = abonoFacturasRepository.findAll();
        assertThat(abonoFacturasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAbonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonoFacturasRepository.findAll().size();
        // set the field null
        abonoFacturas.setAbono(null);

        // Create the AbonoFacturas, which fails.

        restAbonoFacturasMockMvc
            .perform(
                post("/api/abono-facturas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonoFacturas))
            )
            .andExpect(status().isBadRequest());

        List<AbonoFacturas> abonoFacturasList = abonoFacturasRepository.findAll();
        assertThat(abonoFacturasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAbonoFacturas() throws Exception {
        // Initialize the database
        abonoFacturasRepository.saveAndFlush(abonoFacturas);

        // Get all the abonoFacturasList
        restAbonoFacturasMockMvc
            .perform(get("/api/abono-facturas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abonoFacturas.getId().intValue())))
            .andExpect(jsonPath("$.[*].saldoAnterior").value(hasItem(DEFAULT_SALDO_ANTERIOR.intValue())))
            .andExpect(jsonPath("$.[*].abono").value(hasItem(DEFAULT_ABONO.intValue())))
            .andExpect(jsonPath("$.[*].nuevoSaldo").value(hasItem(DEFAULT_NUEVO_SALDO.intValue())));
    }

    @Test
    @Transactional
    public void getAbonoFacturas() throws Exception {
        // Initialize the database
        abonoFacturasRepository.saveAndFlush(abonoFacturas);

        // Get the abonoFacturas
        restAbonoFacturasMockMvc
            .perform(get("/api/abono-facturas/{id}", abonoFacturas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(abonoFacturas.getId().intValue()))
            .andExpect(jsonPath("$.saldoAnterior").value(DEFAULT_SALDO_ANTERIOR.intValue()))
            .andExpect(jsonPath("$.abono").value(DEFAULT_ABONO.intValue()))
            .andExpect(jsonPath("$.nuevoSaldo").value(DEFAULT_NUEVO_SALDO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAbonoFacturas() throws Exception {
        // Get the abonoFacturas
        restAbonoFacturasMockMvc.perform(get("/api/abono-facturas/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbonoFacturas() throws Exception {
        // Initialize the database
        abonoFacturasRepository.saveAndFlush(abonoFacturas);

        int databaseSizeBeforeUpdate = abonoFacturasRepository.findAll().size();

        // Update the abonoFacturas
        AbonoFacturas updatedAbonoFacturas = abonoFacturasRepository.findById(abonoFacturas.getId()).get();
        // Disconnect from session so that the updates on updatedAbonoFacturas are not directly saved in db
        em.detach(updatedAbonoFacturas);
        updatedAbonoFacturas.saldoAnterior(UPDATED_SALDO_ANTERIOR).abono(UPDATED_ABONO).nuevoSaldo(UPDATED_NUEVO_SALDO);

        restAbonoFacturasMockMvc
            .perform(
                put("/api/abono-facturas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAbonoFacturas))
            )
            .andExpect(status().isOk());

        // Validate the AbonoFacturas in the database
        List<AbonoFacturas> abonoFacturasList = abonoFacturasRepository.findAll();
        assertThat(abonoFacturasList).hasSize(databaseSizeBeforeUpdate);
        AbonoFacturas testAbonoFacturas = abonoFacturasList.get(abonoFacturasList.size() - 1);
        assertThat(testAbonoFacturas.getSaldoAnterior()).isEqualTo(UPDATED_SALDO_ANTERIOR);
        assertThat(testAbonoFacturas.getAbono()).isEqualTo(UPDATED_ABONO);
        assertThat(testAbonoFacturas.getNuevoSaldo()).isEqualTo(UPDATED_NUEVO_SALDO);
    }

    @Test
    @Transactional
    public void updateNonExistingAbonoFacturas() throws Exception {
        int databaseSizeBeforeUpdate = abonoFacturasRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonoFacturasMockMvc
            .perform(
                put("/api/abono-facturas").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonoFacturas))
            )
            .andExpect(status().isBadRequest());

        // Validate the AbonoFacturas in the database
        List<AbonoFacturas> abonoFacturasList = abonoFacturasRepository.findAll();
        assertThat(abonoFacturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAbonoFacturas() throws Exception {
        // Initialize the database
        abonoFacturasRepository.saveAndFlush(abonoFacturas);

        int databaseSizeBeforeDelete = abonoFacturasRepository.findAll().size();

        // Delete the abonoFacturas
        restAbonoFacturasMockMvc
            .perform(delete("/api/abono-facturas/{id}", abonoFacturas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AbonoFacturas> abonoFacturasList = abonoFacturasRepository.findAll();
        assertThat(abonoFacturasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
