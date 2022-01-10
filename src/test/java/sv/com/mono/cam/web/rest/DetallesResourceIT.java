package sv.com.mono.cam.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import sv.com.mono.cam.domain.Detalles;
import sv.com.mono.cam.domain.Facturas;
import sv.com.mono.cam.domain.Lotes;
import sv.com.mono.cam.repository.DetallesRepository;
import sv.com.mono.cam.service.criteria.DetallesCriteria;

/**
 * Integration tests for the {@link DetallesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetallesResourceIT {

    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;
    private static final Long SMALLER_CANTIDAD = 1L - 1L;

    private static final Long DEFAULT_TOTAL = 1L;
    private static final Long UPDATED_TOTAL = 2L;
    private static final Long SMALLER_TOTAL = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/detalles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetallesRepository detallesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetallesMockMvc;

    private Detalles detalles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Detalles createEntity(EntityManager em) {
        Detalles detalles = new Detalles().cantidad(DEFAULT_CANTIDAD).total(DEFAULT_TOTAL);
        return detalles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Detalles createUpdatedEntity(EntityManager em) {
        Detalles detalles = new Detalles().cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);
        return detalles;
    }

    @BeforeEach
    public void initTest() {
        detalles = createEntity(em);
    }

    @Test
    @Transactional
    void createDetalles() throws Exception {
        int databaseSizeBeforeCreate = detallesRepository.findAll().size();
        // Create the Detalles
        restDetallesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalles)))
            .andExpect(status().isCreated());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeCreate + 1);
        Detalles testDetalles = detallesList.get(detallesList.size() - 1);
        assertThat(testDetalles.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testDetalles.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void createDetallesWithExistingId() throws Exception {
        // Create the Detalles with an existing ID
        detalles.setId(1L);

        int databaseSizeBeforeCreate = detallesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetallesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalles)))
            .andExpect(status().isBadRequest());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = detallesRepository.findAll().size();
        // set the field null
        detalles.setCantidad(null);

        // Create the Detalles, which fails.

        restDetallesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalles)))
            .andExpect(status().isBadRequest());

        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetalles() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList
        restDetallesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalles.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }

    @Test
    @Transactional
    void getDetalles() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get the detalles
        restDetallesMockMvc
            .perform(get(ENTITY_API_URL_ID, detalles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detalles.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()));
    }

    @Test
    @Transactional
    void getDetallesByIdFiltering() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        Long id = detalles.getId();

        defaultDetallesShouldBeFound("id.equals=" + id);
        defaultDetallesShouldNotBeFound("id.notEquals=" + id);

        defaultDetallesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDetallesShouldNotBeFound("id.greaterThan=" + id);

        defaultDetallesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDetallesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDetallesByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where cantidad equals to DEFAULT_CANTIDAD
        defaultDetallesShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the detallesList where cantidad equals to UPDATED_CANTIDAD
        defaultDetallesShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetallesByCantidadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where cantidad not equals to DEFAULT_CANTIDAD
        defaultDetallesShouldNotBeFound("cantidad.notEquals=" + DEFAULT_CANTIDAD);

        // Get all the detallesList where cantidad not equals to UPDATED_CANTIDAD
        defaultDetallesShouldBeFound("cantidad.notEquals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetallesByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultDetallesShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the detallesList where cantidad equals to UPDATED_CANTIDAD
        defaultDetallesShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetallesByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where cantidad is not null
        defaultDetallesShouldBeFound("cantidad.specified=true");

        // Get all the detallesList where cantidad is null
        defaultDetallesShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllDetallesByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultDetallesShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the detallesList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultDetallesShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetallesByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultDetallesShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the detallesList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultDetallesShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetallesByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where cantidad is less than DEFAULT_CANTIDAD
        defaultDetallesShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the detallesList where cantidad is less than UPDATED_CANTIDAD
        defaultDetallesShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetallesByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where cantidad is greater than DEFAULT_CANTIDAD
        defaultDetallesShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the detallesList where cantidad is greater than SMALLER_CANTIDAD
        defaultDetallesShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetallesByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where total equals to DEFAULT_TOTAL
        defaultDetallesShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the detallesList where total equals to UPDATED_TOTAL
        defaultDetallesShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetallesByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where total not equals to DEFAULT_TOTAL
        defaultDetallesShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the detallesList where total not equals to UPDATED_TOTAL
        defaultDetallesShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetallesByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultDetallesShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the detallesList where total equals to UPDATED_TOTAL
        defaultDetallesShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetallesByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where total is not null
        defaultDetallesShouldBeFound("total.specified=true");

        // Get all the detallesList where total is null
        defaultDetallesShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllDetallesByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where total is greater than or equal to DEFAULT_TOTAL
        defaultDetallesShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the detallesList where total is greater than or equal to UPDATED_TOTAL
        defaultDetallesShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetallesByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where total is less than or equal to DEFAULT_TOTAL
        defaultDetallesShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the detallesList where total is less than or equal to SMALLER_TOTAL
        defaultDetallesShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetallesByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where total is less than DEFAULT_TOTAL
        defaultDetallesShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the detallesList where total is less than UPDATED_TOTAL
        defaultDetallesShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetallesByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        // Get all the detallesList where total is greater than DEFAULT_TOTAL
        defaultDetallesShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the detallesList where total is greater than SMALLER_TOTAL
        defaultDetallesShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetallesByFacturasIsEqualToSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);
        Facturas facturas = FacturasResourceIT.createEntity(em);
        em.persist(facturas);
        em.flush();
        detalles.setFacturas(facturas);
        detallesRepository.saveAndFlush(detalles);
        Long facturasId = facturas.getId();

        // Get all the detallesList where facturas equals to facturasId
        defaultDetallesShouldBeFound("facturasId.equals=" + facturasId);

        // Get all the detallesList where facturas equals to (facturasId + 1)
        defaultDetallesShouldNotBeFound("facturasId.equals=" + (facturasId + 1));
    }

    @Test
    @Transactional
    void getAllDetallesByLotesIsEqualToSomething() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);
        Lotes lotes = LotesResourceIT.createEntity(em);
        em.persist(lotes);
        em.flush();
        detalles.setLotes(lotes);
        detallesRepository.saveAndFlush(detalles);
        Long lotesId = lotes.getId();

        // Get all the detallesList where lotes equals to lotesId
        defaultDetallesShouldBeFound("lotesId.equals=" + lotesId);

        // Get all the detallesList where lotes equals to (lotesId + 1)
        defaultDetallesShouldNotBeFound("lotesId.equals=" + (lotesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDetallesShouldBeFound(String filter) throws Exception {
        restDetallesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalles.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));

        // Check, that the count call also returns 1
        restDetallesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDetallesShouldNotBeFound(String filter) throws Exception {
        restDetallesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDetallesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDetalles() throws Exception {
        // Get the detalles
        restDetallesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetalles() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        int databaseSizeBeforeUpdate = detallesRepository.findAll().size();

        // Update the detalles
        Detalles updatedDetalles = detallesRepository.findById(detalles.getId()).get();
        // Disconnect from session so that the updates on updatedDetalles are not directly saved in db
        em.detach(updatedDetalles);
        updatedDetalles.cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);

        restDetallesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetalles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetalles))
            )
            .andExpect(status().isOk());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeUpdate);
        Detalles testDetalles = detallesList.get(detallesList.size() - 1);
        assertThat(testDetalles.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testDetalles.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingDetalles() throws Exception {
        int databaseSizeBeforeUpdate = detallesRepository.findAll().size();
        detalles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetallesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detalles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detalles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetalles() throws Exception {
        int databaseSizeBeforeUpdate = detallesRepository.findAll().size();
        detalles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetallesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detalles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetalles() throws Exception {
        int databaseSizeBeforeUpdate = detallesRepository.findAll().size();
        detalles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetallesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetallesWithPatch() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        int databaseSizeBeforeUpdate = detallesRepository.findAll().size();

        // Update the detalles using partial update
        Detalles partialUpdatedDetalles = new Detalles();
        partialUpdatedDetalles.setId(detalles.getId());

        restDetallesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalles))
            )
            .andExpect(status().isOk());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeUpdate);
        Detalles testDetalles = detallesList.get(detallesList.size() - 1);
        assertThat(testDetalles.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testDetalles.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateDetallesWithPatch() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        int databaseSizeBeforeUpdate = detallesRepository.findAll().size();

        // Update the detalles using partial update
        Detalles partialUpdatedDetalles = new Detalles();
        partialUpdatedDetalles.setId(detalles.getId());

        partialUpdatedDetalles.cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);

        restDetallesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalles))
            )
            .andExpect(status().isOk());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeUpdate);
        Detalles testDetalles = detallesList.get(detallesList.size() - 1);
        assertThat(testDetalles.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testDetalles.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingDetalles() throws Exception {
        int databaseSizeBeforeUpdate = detallesRepository.findAll().size();
        detalles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetallesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detalles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detalles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetalles() throws Exception {
        int databaseSizeBeforeUpdate = detallesRepository.findAll().size();
        detalles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetallesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detalles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetalles() throws Exception {
        int databaseSizeBeforeUpdate = detallesRepository.findAll().size();
        detalles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetallesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(detalles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Detalles in the database
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetalles() throws Exception {
        // Initialize the database
        detallesRepository.saveAndFlush(detalles);

        int databaseSizeBeforeDelete = detallesRepository.findAll().size();

        // Delete the detalles
        restDetallesMockMvc
            .perform(delete(ENTITY_API_URL_ID, detalles.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Detalles> detallesList = detallesRepository.findAll();
        assertThat(detallesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
