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
import sv.com.mono.cam.domain.Abonos;
import sv.com.mono.cam.domain.Clientes;
import sv.com.mono.cam.domain.Detalles;
import sv.com.mono.cam.domain.Facturas;
import sv.com.mono.cam.repository.FacturasRepository;
import sv.com.mono.cam.service.criteria.FacturasCriteria;

/**
 * Integration tests for the {@link FacturasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturasResourceIT {

    private static final Long DEFAULT_NUMERO_FACTURA = 1L;
    private static final Long UPDATED_NUMERO_FACTURA = 2L;
    private static final Long SMALLER_NUMERO_FACTURA = 1L - 1L;

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
    void getFacturasByIdFiltering() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        Long id = facturas.getId();

        defaultFacturasShouldBeFound("id.equals=" + id);
        defaultFacturasShouldNotBeFound("id.notEquals=" + id);

        defaultFacturasShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacturasShouldNotBeFound("id.greaterThan=" + id);

        defaultFacturasShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacturasShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroFacturaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where numeroFactura equals to DEFAULT_NUMERO_FACTURA
        defaultFacturasShouldBeFound("numeroFactura.equals=" + DEFAULT_NUMERO_FACTURA);

        // Get all the facturasList where numeroFactura equals to UPDATED_NUMERO_FACTURA
        defaultFacturasShouldNotBeFound("numeroFactura.equals=" + UPDATED_NUMERO_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroFacturaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where numeroFactura not equals to DEFAULT_NUMERO_FACTURA
        defaultFacturasShouldNotBeFound("numeroFactura.notEquals=" + DEFAULT_NUMERO_FACTURA);

        // Get all the facturasList where numeroFactura not equals to UPDATED_NUMERO_FACTURA
        defaultFacturasShouldBeFound("numeroFactura.notEquals=" + UPDATED_NUMERO_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroFacturaIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where numeroFactura in DEFAULT_NUMERO_FACTURA or UPDATED_NUMERO_FACTURA
        defaultFacturasShouldBeFound("numeroFactura.in=" + DEFAULT_NUMERO_FACTURA + "," + UPDATED_NUMERO_FACTURA);

        // Get all the facturasList where numeroFactura equals to UPDATED_NUMERO_FACTURA
        defaultFacturasShouldNotBeFound("numeroFactura.in=" + UPDATED_NUMERO_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroFacturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where numeroFactura is not null
        defaultFacturasShouldBeFound("numeroFactura.specified=true");

        // Get all the facturasList where numeroFactura is null
        defaultFacturasShouldNotBeFound("numeroFactura.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroFacturaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where numeroFactura is greater than or equal to DEFAULT_NUMERO_FACTURA
        defaultFacturasShouldBeFound("numeroFactura.greaterThanOrEqual=" + DEFAULT_NUMERO_FACTURA);

        // Get all the facturasList where numeroFactura is greater than or equal to UPDATED_NUMERO_FACTURA
        defaultFacturasShouldNotBeFound("numeroFactura.greaterThanOrEqual=" + UPDATED_NUMERO_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroFacturaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where numeroFactura is less than or equal to DEFAULT_NUMERO_FACTURA
        defaultFacturasShouldBeFound("numeroFactura.lessThanOrEqual=" + DEFAULT_NUMERO_FACTURA);

        // Get all the facturasList where numeroFactura is less than or equal to SMALLER_NUMERO_FACTURA
        defaultFacturasShouldNotBeFound("numeroFactura.lessThanOrEqual=" + SMALLER_NUMERO_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroFacturaIsLessThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where numeroFactura is less than DEFAULT_NUMERO_FACTURA
        defaultFacturasShouldNotBeFound("numeroFactura.lessThan=" + DEFAULT_NUMERO_FACTURA);

        // Get all the facturasList where numeroFactura is less than UPDATED_NUMERO_FACTURA
        defaultFacturasShouldBeFound("numeroFactura.lessThan=" + UPDATED_NUMERO_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroFacturaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where numeroFactura is greater than DEFAULT_NUMERO_FACTURA
        defaultFacturasShouldNotBeFound("numeroFactura.greaterThan=" + DEFAULT_NUMERO_FACTURA);

        // Get all the facturasList where numeroFactura is greater than SMALLER_NUMERO_FACTURA
        defaultFacturasShouldBeFound("numeroFactura.greaterThan=" + SMALLER_NUMERO_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaFacturaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fechaFactura equals to DEFAULT_FECHA_FACTURA
        defaultFacturasShouldBeFound("fechaFactura.equals=" + DEFAULT_FECHA_FACTURA);

        // Get all the facturasList where fechaFactura equals to UPDATED_FECHA_FACTURA
        defaultFacturasShouldNotBeFound("fechaFactura.equals=" + UPDATED_FECHA_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaFacturaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fechaFactura not equals to DEFAULT_FECHA_FACTURA
        defaultFacturasShouldNotBeFound("fechaFactura.notEquals=" + DEFAULT_FECHA_FACTURA);

        // Get all the facturasList where fechaFactura not equals to UPDATED_FECHA_FACTURA
        defaultFacturasShouldBeFound("fechaFactura.notEquals=" + UPDATED_FECHA_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaFacturaIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fechaFactura in DEFAULT_FECHA_FACTURA or UPDATED_FECHA_FACTURA
        defaultFacturasShouldBeFound("fechaFactura.in=" + DEFAULT_FECHA_FACTURA + "," + UPDATED_FECHA_FACTURA);

        // Get all the facturasList where fechaFactura equals to UPDATED_FECHA_FACTURA
        defaultFacturasShouldNotBeFound("fechaFactura.in=" + UPDATED_FECHA_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaFacturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where fechaFactura is not null
        defaultFacturasShouldBeFound("fechaFactura.specified=true");

        // Get all the facturasList where fechaFactura is null
        defaultFacturasShouldNotBeFound("fechaFactura.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByCondicionPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where condicionPago equals to DEFAULT_CONDICION_PAGO
        defaultFacturasShouldBeFound("condicionPago.equals=" + DEFAULT_CONDICION_PAGO);

        // Get all the facturasList where condicionPago equals to UPDATED_CONDICION_PAGO
        defaultFacturasShouldNotBeFound("condicionPago.equals=" + UPDATED_CONDICION_PAGO);
    }

    @Test
    @Transactional
    void getAllFacturasByCondicionPagoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where condicionPago not equals to DEFAULT_CONDICION_PAGO
        defaultFacturasShouldNotBeFound("condicionPago.notEquals=" + DEFAULT_CONDICION_PAGO);

        // Get all the facturasList where condicionPago not equals to UPDATED_CONDICION_PAGO
        defaultFacturasShouldBeFound("condicionPago.notEquals=" + UPDATED_CONDICION_PAGO);
    }

    @Test
    @Transactional
    void getAllFacturasByCondicionPagoIsInShouldWork() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where condicionPago in DEFAULT_CONDICION_PAGO or UPDATED_CONDICION_PAGO
        defaultFacturasShouldBeFound("condicionPago.in=" + DEFAULT_CONDICION_PAGO + "," + UPDATED_CONDICION_PAGO);

        // Get all the facturasList where condicionPago equals to UPDATED_CONDICION_PAGO
        defaultFacturasShouldNotBeFound("condicionPago.in=" + UPDATED_CONDICION_PAGO);
    }

    @Test
    @Transactional
    void getAllFacturasByCondicionPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);

        // Get all the facturasList where condicionPago is not null
        defaultFacturasShouldBeFound("condicionPago.specified=true");

        // Get all the facturasList where condicionPago is null
        defaultFacturasShouldNotBeFound("condicionPago.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByClientesIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);
        Clientes clientes = ClientesResourceIT.createEntity(em);
        em.persist(clientes);
        em.flush();
        facturas.setClientes(clientes);
        facturasRepository.saveAndFlush(facturas);
        Long clientesId = clientes.getId();

        // Get all the facturasList where clientes equals to clientesId
        defaultFacturasShouldBeFound("clientesId.equals=" + clientesId);

        // Get all the facturasList where clientes equals to (clientesId + 1)
        defaultFacturasShouldNotBeFound("clientesId.equals=" + (clientesId + 1));
    }

    @Test
    @Transactional
    void getAllFacturasByDetallesIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);
        Detalles detalles = DetallesResourceIT.createEntity(em);
        em.persist(detalles);
        em.flush();
        facturas.addDetalles(detalles);
        facturasRepository.saveAndFlush(facturas);
        Long detallesId = detalles.getId();

        // Get all the facturasList where detalles equals to detallesId
        defaultFacturasShouldBeFound("detallesId.equals=" + detallesId);

        // Get all the facturasList where detalles equals to (detallesId + 1)
        defaultFacturasShouldNotBeFound("detallesId.equals=" + (detallesId + 1));
    }

    @Test
    @Transactional
    void getAllFacturasByAbonosIsEqualToSomething() throws Exception {
        // Initialize the database
        facturasRepository.saveAndFlush(facturas);
        Abonos abonos = AbonosResourceIT.createEntity(em);
        em.persist(abonos);
        em.flush();
        facturas.addAbonos(abonos);
        facturasRepository.saveAndFlush(facturas);
        Long abonosId = abonos.getId();

        // Get all the facturasList where abonos equals to abonosId
        defaultFacturasShouldBeFound("abonosId.equals=" + abonosId);

        // Get all the facturasList where abonos equals to (abonosId + 1)
        defaultFacturasShouldNotBeFound("abonosId.equals=" + (abonosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacturasShouldBeFound(String filter) throws Exception {
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturas.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroFactura").value(hasItem(DEFAULT_NUMERO_FACTURA.intValue())))
            .andExpect(jsonPath("$.[*].fechaFactura").value(hasItem(DEFAULT_FECHA_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].condicionPago").value(hasItem(DEFAULT_CONDICION_PAGO.booleanValue())));

        // Check, that the count call also returns 1
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacturasShouldNotBeFound(String filter) throws Exception {
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacturasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
