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
import sv.com.mono.cam.domain.Lotes;
import sv.com.mono.cam.repository.LotesRepository;

/**
 * Integration tests for the {@link LotesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LotesResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final LocalDate DEFAULT_FECHA_ENTRADA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ENTRADA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LOTE = "AAAAAAAAAA";
    private static final String UPDATED_LOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lotes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LotesRepository lotesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLotesMockMvc;

    private Lotes lotes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lotes createEntity(EntityManager em) {
        Lotes lotes = new Lotes().cantidad(DEFAULT_CANTIDAD).fechaEntrada(DEFAULT_FECHA_ENTRADA).lote(DEFAULT_LOTE);
        return lotes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lotes createUpdatedEntity(EntityManager em) {
        Lotes lotes = new Lotes().cantidad(UPDATED_CANTIDAD).fechaEntrada(UPDATED_FECHA_ENTRADA).lote(UPDATED_LOTE);
        return lotes;
    }

    @BeforeEach
    public void initTest() {
        lotes = createEntity(em);
    }

    @Test
    @Transactional
    void createLotes() throws Exception {
        int databaseSizeBeforeCreate = lotesRepository.findAll().size();
        // Create the Lotes
        restLotesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lotes)))
            .andExpect(status().isCreated());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeCreate + 1);
        Lotes testLotes = lotesList.get(lotesList.size() - 1);
        assertThat(testLotes.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testLotes.getFechaEntrada()).isEqualTo(DEFAULT_FECHA_ENTRADA);
        assertThat(testLotes.getLote()).isEqualTo(DEFAULT_LOTE);
    }

    @Test
    @Transactional
    void createLotesWithExistingId() throws Exception {
        // Create the Lotes with an existing ID
        lotes.setId(1L);

        int databaseSizeBeforeCreate = lotesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLotesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lotes)))
            .andExpect(status().isBadRequest());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLotes() throws Exception {
        // Initialize the database
        lotesRepository.saveAndFlush(lotes);

        // Get all the lotesList
        restLotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lotes.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].fechaEntrada").value(hasItem(DEFAULT_FECHA_ENTRADA.toString())))
            .andExpect(jsonPath("$.[*].lote").value(hasItem(DEFAULT_LOTE)));
    }

    @Test
    @Transactional
    void getLotes() throws Exception {
        // Initialize the database
        lotesRepository.saveAndFlush(lotes);

        // Get the lotes
        restLotesMockMvc
            .perform(get(ENTITY_API_URL_ID, lotes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lotes.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.fechaEntrada").value(DEFAULT_FECHA_ENTRADA.toString()))
            .andExpect(jsonPath("$.lote").value(DEFAULT_LOTE));
    }

    @Test
    @Transactional
    void getNonExistingLotes() throws Exception {
        // Get the lotes
        restLotesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLotes() throws Exception {
        // Initialize the database
        lotesRepository.saveAndFlush(lotes);

        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();

        // Update the lotes
        Lotes updatedLotes = lotesRepository.findById(lotes.getId()).get();
        // Disconnect from session so that the updates on updatedLotes are not directly saved in db
        em.detach(updatedLotes);
        updatedLotes.cantidad(UPDATED_CANTIDAD).fechaEntrada(UPDATED_FECHA_ENTRADA).lote(UPDATED_LOTE);

        restLotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLotes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLotes))
            )
            .andExpect(status().isOk());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
        Lotes testLotes = lotesList.get(lotesList.size() - 1);
        assertThat(testLotes.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testLotes.getFechaEntrada()).isEqualTo(UPDATED_FECHA_ENTRADA);
        assertThat(testLotes.getLote()).isEqualTo(UPDATED_LOTE);
    }

    @Test
    @Transactional
    void putNonExistingLotes() throws Exception {
        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();
        lotes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lotes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLotes() throws Exception {
        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();
        lotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLotes() throws Exception {
        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();
        lotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lotes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLotesWithPatch() throws Exception {
        // Initialize the database
        lotesRepository.saveAndFlush(lotes);

        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();

        // Update the lotes using partial update
        Lotes partialUpdatedLotes = new Lotes();
        partialUpdatedLotes.setId(lotes.getId());

        partialUpdatedLotes.cantidad(UPDATED_CANTIDAD).fechaEntrada(UPDATED_FECHA_ENTRADA).lote(UPDATED_LOTE);

        restLotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLotes))
            )
            .andExpect(status().isOk());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
        Lotes testLotes = lotesList.get(lotesList.size() - 1);
        assertThat(testLotes.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testLotes.getFechaEntrada()).isEqualTo(UPDATED_FECHA_ENTRADA);
        assertThat(testLotes.getLote()).isEqualTo(UPDATED_LOTE);
    }

    @Test
    @Transactional
    void fullUpdateLotesWithPatch() throws Exception {
        // Initialize the database
        lotesRepository.saveAndFlush(lotes);

        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();

        // Update the lotes using partial update
        Lotes partialUpdatedLotes = new Lotes();
        partialUpdatedLotes.setId(lotes.getId());

        partialUpdatedLotes.cantidad(UPDATED_CANTIDAD).fechaEntrada(UPDATED_FECHA_ENTRADA).lote(UPDATED_LOTE);

        restLotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLotes))
            )
            .andExpect(status().isOk());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
        Lotes testLotes = lotesList.get(lotesList.size() - 1);
        assertThat(testLotes.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testLotes.getFechaEntrada()).isEqualTo(UPDATED_FECHA_ENTRADA);
        assertThat(testLotes.getLote()).isEqualTo(UPDATED_LOTE);
    }

    @Test
    @Transactional
    void patchNonExistingLotes() throws Exception {
        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();
        lotes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLotes() throws Exception {
        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();
        lotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLotes() throws Exception {
        int databaseSizeBeforeUpdate = lotesRepository.findAll().size();
        lotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lotes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lotes in the database
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLotes() throws Exception {
        // Initialize the database
        lotesRepository.saveAndFlush(lotes);

        int databaseSizeBeforeDelete = lotesRepository.findAll().size();

        // Delete the lotes
        restLotesMockMvc
            .perform(delete(ENTITY_API_URL_ID, lotes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lotes> lotesList = lotesRepository.findAll();
        assertThat(lotesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
