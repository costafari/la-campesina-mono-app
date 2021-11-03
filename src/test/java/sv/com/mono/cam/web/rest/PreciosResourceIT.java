package sv.com.mono.cam.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import sv.com.mono.cam.domain.Precios;
import sv.com.mono.cam.repository.PreciosRepository;

/**
 * Integration tests for the {@link PreciosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PreciosResourceIT {

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_PRECIO = 1L;
    private static final Long UPDATED_PRECIO = 2L;

    private static final String ENTITY_API_URL = "/api/precios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        Precios precios = new Precios().fechaFin(DEFAULT_FECHA_FIN).fechaInicio(DEFAULT_FECHA_INICIO).precio(DEFAULT_PRECIO);
        return precios;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Precios createUpdatedEntity(EntityManager em) {
        Precios precios = new Precios().fechaFin(UPDATED_FECHA_FIN).fechaInicio(UPDATED_FECHA_INICIO).precio(UPDATED_PRECIO);
        return precios;
    }

    @BeforeEach
    public void initTest() {
        precios = createEntity(em);
    }

    @Test
    @Transactional
    void createPrecios() throws Exception {
        int databaseSizeBeforeCreate = preciosRepository.findAll().size();
        // Create the Precios
        restPreciosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(precios)))
            .andExpect(status().isCreated());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeCreate + 1);
        Precios testPrecios = preciosList.get(preciosList.size() - 1);
        assertThat(testPrecios.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testPrecios.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testPrecios.getPrecio()).isEqualTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    void createPreciosWithExistingId() throws Exception {
        // Create the Precios with an existing ID
        precios.setId(1L);

        int databaseSizeBeforeCreate = preciosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreciosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(precios)))
            .andExpect(status().isBadRequest());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrecios() throws Exception {
        // Initialize the database
        preciosRepository.saveAndFlush(precios);

        // Get all the preciosList
        restPreciosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(precios.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.intValue())));
    }

    @Test
    @Transactional
    void getPrecios() throws Exception {
        // Initialize the database
        preciosRepository.saveAndFlush(precios);

        // Get the precios
        restPreciosMockMvc
            .perform(get(ENTITY_API_URL_ID, precios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(precios.getId().intValue()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPrecios() throws Exception {
        // Get the precios
        restPreciosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrecios() throws Exception {
        // Initialize the database
        preciosRepository.saveAndFlush(precios);

        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();

        // Update the precios
        Precios updatedPrecios = preciosRepository.findById(precios.getId()).get();
        // Disconnect from session so that the updates on updatedPrecios are not directly saved in db
        em.detach(updatedPrecios);
        updatedPrecios.fechaFin(UPDATED_FECHA_FIN).fechaInicio(UPDATED_FECHA_INICIO).precio(UPDATED_PRECIO);

        restPreciosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrecios.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrecios))
            )
            .andExpect(status().isOk());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
        Precios testPrecios = preciosList.get(preciosList.size() - 1);
        assertThat(testPrecios.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testPrecios.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testPrecios.getPrecio()).isEqualTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void putNonExistingPrecios() throws Exception {
        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();
        precios.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreciosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, precios.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(precios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrecios() throws Exception {
        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();
        precios.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreciosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(precios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrecios() throws Exception {
        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();
        precios.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreciosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(precios)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePreciosWithPatch() throws Exception {
        // Initialize the database
        preciosRepository.saveAndFlush(precios);

        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();

        // Update the precios using partial update
        Precios partialUpdatedPrecios = new Precios();
        partialUpdatedPrecios.setId(precios.getId());

        partialUpdatedPrecios.fechaFin(UPDATED_FECHA_FIN);

        restPreciosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrecios.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrecios))
            )
            .andExpect(status().isOk());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
        Precios testPrecios = preciosList.get(preciosList.size() - 1);
        assertThat(testPrecios.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testPrecios.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testPrecios.getPrecio()).isEqualTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    void fullUpdatePreciosWithPatch() throws Exception {
        // Initialize the database
        preciosRepository.saveAndFlush(precios);

        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();

        // Update the precios using partial update
        Precios partialUpdatedPrecios = new Precios();
        partialUpdatedPrecios.setId(precios.getId());

        partialUpdatedPrecios.fechaFin(UPDATED_FECHA_FIN).fechaInicio(UPDATED_FECHA_INICIO).precio(UPDATED_PRECIO);

        restPreciosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrecios.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrecios))
            )
            .andExpect(status().isOk());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
        Precios testPrecios = preciosList.get(preciosList.size() - 1);
        assertThat(testPrecios.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testPrecios.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testPrecios.getPrecio()).isEqualTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void patchNonExistingPrecios() throws Exception {
        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();
        precios.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreciosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, precios.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(precios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrecios() throws Exception {
        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();
        precios.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreciosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(precios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrecios() throws Exception {
        int databaseSizeBeforeUpdate = preciosRepository.findAll().size();
        precios.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreciosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(precios)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Precios in the database
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrecios() throws Exception {
        // Initialize the database
        preciosRepository.saveAndFlush(precios);

        int databaseSizeBeforeDelete = preciosRepository.findAll().size();

        // Delete the precios
        restPreciosMockMvc
            .perform(delete(ENTITY_API_URL_ID, precios.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Precios> preciosList = preciosRepository.findAll();
        assertThat(preciosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
