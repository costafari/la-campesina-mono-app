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
import sv.com.mono.cam.repository.DetallesRepository;

/**
 * Integration tests for the {@link DetallesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetallesResourceIT {

    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;

    private static final Long DEFAULT_TOTAL = 1L;
    private static final Long UPDATED_TOTAL = 2L;

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
