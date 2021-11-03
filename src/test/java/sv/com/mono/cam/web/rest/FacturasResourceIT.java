package sv.com.mono.cam.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sv.com.mono.cam.IntegrationTest;
import sv.com.mono.cam.domain.Facturas;
import sv.com.mono.cam.repository.FacturasRepository;

/**
 * Integration tests for the {@link FacturasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturasResourceIT {

    private static final Long DEFAULT_NUMERO_FACTURA = 1L;
    private static final Long UPDATED_NUMERO_FACTURA = 2L;

    private static final Instant DEFAULT_FECHA_FACTURA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FACTURA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_CONDICION_PAGO = false;
    private static final Boolean UPDATED_CONDICION_PAGO = true;

    private static final String ENTITY_API_URL = "/api/facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturasRepository facturasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturasMockMvc;

    private Facturas facturas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facturas createEntity(EntityManager em) {
        Facturas facturas = new Facturas()
            .numeroFactura(DEFAULT_NUMERO_FACTURA)
            .fechaFactura(DEFAULT_FECHA_FACTURA)
            .condicionPago(DEFAULT_CONDICION_PAGO);
        return facturas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facturas createUpdatedEntity(EntityManager em) {
        Facturas facturas = new Facturas()
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaFactura(UPDATED_FECHA_FACTURA)
            .condicionPago(UPDATED_CONDICION_PAGO);
        return facturas;
    }

    @BeforeEach
    public void initTest() {
        facturas = createEntity(em);
    }

    @Test
    @Transactional
    void createFacturas() throws Exception {
        int databaseSizeBeforeCreate = facturasRepository.findAll().size();
        // Create the Facturas
        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isCreated());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeCreate + 1);
        Facturas testFacturas = facturasList.get(facturasList.size() - 1);
        assertThat(testFacturas.getNumeroFactura()).isEqualTo(DEFAULT_NUMERO_FACTURA);
        assertThat(testFacturas.getFechaFactura()).isEqualTo(DEFAULT_FECHA_FACTURA);
        assertThat(testFacturas.getCondicionPago()).isEqualTo(DEFAULT_CONDICION_PAGO);
    }

    @Test
    @Transactional
    void createFacturasWithExistingId() throws Exception {
        // Create the Facturas with an existing ID
        facturas.setId(1L);

        int databaseSizeBeforeCreate = facturasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isBadRequest());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroFacturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturasRepository.findAll().size();
        // set the field null
        facturas.setNumeroFactura(null);

        // Create the Facturas, which fails.

        restFacturasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isBadRequest());

        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacturas() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturas.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroFactura").value(hasItem(DEFAULT_NUMERO_FACTURA.intValue())))
            .andExpect(jsonPath("$.[*].fechaFactura").value(hasItem(DEFAULT_FECHA_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].condicionPago").value(hasItem(DEFAULT_CONDICION_PAGO.booleanValue())));
    }

    @Test
    @Transactional
    void getFacturas() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get the facturas
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL_ID, facturas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facturas.getId().intValue()))
            .andExpect(jsonPath("$.numeroFactura").value(DEFAULT_NUMERO_FACTURA.intValue()))
            .andExpect(jsonPath("$.fechaFactura").value(DEFAULT_FECHA_FACTURA.toString()))
            .andExpect(jsonPath("$.condicionPago").value(DEFAULT_CONDICION_PAGO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFacturas() throws Exception {
        // Get the facturas
        restFacturasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFacturas() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();

        // Update the facturas
        Facturas updatedFacturas = facturasRepository.findById(facturas.getId()).get();
        // Disconnect from session so that the updates on updatedFacturas are not directly saved in db
        em.detach(updatedFacturas);
        updatedFacturas.numeroFactura(UPDATED_NUMERO_FACTURA).fechaFactura(UPDATED_FECHA_FACTURA).condicionPago(UPDATED_CONDICION_PAGO);

        restFacturasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacturas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFacturas))
            )
            .andExpect(status().isOk());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
        Facturas testFacturas = facturasList.get(facturasList.size() - 1);
        assertThat(testFacturas.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFacturas.getFechaFactura()).isEqualTo(UPDATED_FECHA_FACTURA);
        assertThat(testFacturas.getCondicionPago()).isEqualTo(UPDATED_CONDICION_PAGO);
    }

    @Test
    @Transactional
    void putNonExistingFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturasWithPatch() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();

        // Update the facturas using partial update
        Facturas partialUpdatedFacturas = new Facturas();
        partialUpdatedFacturas.setId(facturas.getId());

        partialUpdatedFacturas.fechaFactura(UPDATED_FECHA_FACTURA).condicionPago(UPDATED_CONDICION_PAGO);

        restFacturasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturas))
            )
            .andExpect(status().isOk());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
        Facturas testFacturas = facturasList.get(facturasList.size() - 1);
        assertThat(testFacturas.getNumeroFactura()).isEqualTo(DEFAULT_NUMERO_FACTURA);
        assertThat(testFacturas.getFechaFactura()).isEqualTo(UPDATED_FECHA_FACTURA);
        assertThat(testFacturas.getCondicionPago()).isEqualTo(UPDATED_CONDICION_PAGO);
    }

    @Test
    @Transactional
    void fullUpdateFacturasWithPatch() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();

        // Update the facturas using partial update
        Facturas partialUpdatedFacturas = new Facturas();
        partialUpdatedFacturas.setId(facturas.getId());

        partialUpdatedFacturas
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaFactura(UPDATED_FECHA_FACTURA)
            .condicionPago(UPDATED_CONDICION_PAGO);

        restFacturasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturas))
            )
            .andExpect(status().isOk());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
        Facturas testFacturas = facturasList.get(facturasList.size() - 1);
        assertThat(testFacturas.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFacturas.getFechaFactura()).isEqualTo(UPDATED_FECHA_FACTURA);
        assertThat(testFacturas.getCondicionPago()).isEqualTo(UPDATED_CONDICION_PAGO);
    }

    @Test
    @Transactional
    void patchNonExistingFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facturas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacturas() throws Exception {
        int databaseSizeBeforeUpdate = facturasRepository.findAll().size();
        facturas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(facturas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facturas in the database
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacturas() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        int databaseSizeBeforeDelete = facturasRepository.findAll().size();

        // Delete the facturas
        restFacturasMockMvc
            .perform(delete(ENTITY_API_URL_ID, facturas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Facturas> facturasList = facturasRepository.findAll();
        assertThat(facturasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
