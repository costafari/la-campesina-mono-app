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
import sv.com.mono.cam.domain.Abonos;
import sv.com.mono.cam.repository.AbonosRepository;

/**
 * Integration tests for the {@link AbonosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AbonosResourceIT {

    private static final Long DEFAULT_SALDO_ANTERIOR = 1L;
    private static final Long UPDATED_SALDO_ANTERIOR = 2L;

    private static final Long DEFAULT_ABONO = 1L;
    private static final Long UPDATED_ABONO = 2L;

    private static final Long DEFAULT_NUEVO_SALDO = 1L;
    private static final Long UPDATED_NUEVO_SALDO = 2L;

    private static final String ENTITY_API_URL = "/api/abonos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AbonosRepository abonosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAbonosMockMvc;

    private Abonos abonos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonos createEntity(EntityManager em) {
        Abonos abonos = new Abonos().saldoAnterior(DEFAULT_SALDO_ANTERIOR).abono(DEFAULT_ABONO).nuevoSaldo(DEFAULT_NUEVO_SALDO);
        return abonos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonos createUpdatedEntity(EntityManager em) {
        Abonos abonos = new Abonos().saldoAnterior(UPDATED_SALDO_ANTERIOR).abono(UPDATED_ABONO).nuevoSaldo(UPDATED_NUEVO_SALDO);
        return abonos;
    }

    @BeforeEach
    public void initTest() {
        abonos = createEntity(em);
    }

    @Test
    @Transactional
    void createAbonos() throws Exception {
        int databaseSizeBeforeCreate = abonosRepository.findAll().size();
        // Create the Abonos
        restAbonosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonos)))
            .andExpect(status().isCreated());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeCreate + 1);
        Abonos testAbonos = abonosList.get(abonosList.size() - 1);
        assertThat(testAbonos.getSaldoAnterior()).isEqualTo(DEFAULT_SALDO_ANTERIOR);
        assertThat(testAbonos.getAbono()).isEqualTo(DEFAULT_ABONO);
        assertThat(testAbonos.getNuevoSaldo()).isEqualTo(DEFAULT_NUEVO_SALDO);
    }

    @Test
    @Transactional
    void createAbonosWithExistingId() throws Exception {
        // Create the Abonos with an existing ID
        abonos.setId(1L);

        int databaseSizeBeforeCreate = abonosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbonosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonos)))
            .andExpect(status().isBadRequest());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSaldoAnteriorIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonosRepository.findAll().size();
        // set the field null
        abonos.setSaldoAnterior(null);

        // Create the Abonos, which fails.

        restAbonosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonos)))
            .andExpect(status().isBadRequest());

        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAbonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonosRepository.findAll().size();
        // set the field null
        abonos.setAbono(null);

        // Create the Abonos, which fails.

        restAbonosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonos)))
            .andExpect(status().isBadRequest());

        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAbonos() throws Exception {
        // Initialize the database
        abonosRepository.saveAndFlush(abonos);

        // Get all the abonosList
        restAbonosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abonos.getId().intValue())))
            .andExpect(jsonPath("$.[*].saldoAnterior").value(hasItem(DEFAULT_SALDO_ANTERIOR.intValue())))
            .andExpect(jsonPath("$.[*].abono").value(hasItem(DEFAULT_ABONO.intValue())))
            .andExpect(jsonPath("$.[*].nuevoSaldo").value(hasItem(DEFAULT_NUEVO_SALDO.intValue())));
    }

    @Test
    @Transactional
    void getAbonos() throws Exception {
        // Initialize the database
        abonosRepository.saveAndFlush(abonos);

        // Get the abonos
        restAbonosMockMvc
            .perform(get(ENTITY_API_URL_ID, abonos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(abonos.getId().intValue()))
            .andExpect(jsonPath("$.saldoAnterior").value(DEFAULT_SALDO_ANTERIOR.intValue()))
            .andExpect(jsonPath("$.abono").value(DEFAULT_ABONO.intValue()))
            .andExpect(jsonPath("$.nuevoSaldo").value(DEFAULT_NUEVO_SALDO.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAbonos() throws Exception {
        // Get the abonos
        restAbonosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAbonos() throws Exception {
        // Initialize the database
        abonosRepository.saveAndFlush(abonos);

        int databaseSizeBeforeUpdate = abonosRepository.findAll().size();

        // Update the abonos
        Abonos updatedAbonos = abonosRepository.findById(abonos.getId()).get();
        // Disconnect from session so that the updates on updatedAbonos are not directly saved in db
        em.detach(updatedAbonos);
        updatedAbonos.saldoAnterior(UPDATED_SALDO_ANTERIOR).abono(UPDATED_ABONO).nuevoSaldo(UPDATED_NUEVO_SALDO);

        restAbonosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAbonos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAbonos))
            )
            .andExpect(status().isOk());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeUpdate);
        Abonos testAbonos = abonosList.get(abonosList.size() - 1);
        assertThat(testAbonos.getSaldoAnterior()).isEqualTo(UPDATED_SALDO_ANTERIOR);
        assertThat(testAbonos.getAbono()).isEqualTo(UPDATED_ABONO);
        assertThat(testAbonos.getNuevoSaldo()).isEqualTo(UPDATED_NUEVO_SALDO);
    }

    @Test
    @Transactional
    void putNonExistingAbonos() throws Exception {
        int databaseSizeBeforeUpdate = abonosRepository.findAll().size();
        abonos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, abonos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAbonos() throws Exception {
        int databaseSizeBeforeUpdate = abonosRepository.findAll().size();
        abonos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAbonos() throws Exception {
        int databaseSizeBeforeUpdate = abonosRepository.findAll().size();
        abonos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAbonosWithPatch() throws Exception {
        // Initialize the database
        abonosRepository.saveAndFlush(abonos);

        int databaseSizeBeforeUpdate = abonosRepository.findAll().size();

        // Update the abonos using partial update
        Abonos partialUpdatedAbonos = new Abonos();
        partialUpdatedAbonos.setId(abonos.getId());

        partialUpdatedAbonos.abono(UPDATED_ABONO).nuevoSaldo(UPDATED_NUEVO_SALDO);

        restAbonosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbonos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbonos))
            )
            .andExpect(status().isOk());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeUpdate);
        Abonos testAbonos = abonosList.get(abonosList.size() - 1);
        assertThat(testAbonos.getSaldoAnterior()).isEqualTo(DEFAULT_SALDO_ANTERIOR);
        assertThat(testAbonos.getAbono()).isEqualTo(UPDATED_ABONO);
        assertThat(testAbonos.getNuevoSaldo()).isEqualTo(UPDATED_NUEVO_SALDO);
    }

    @Test
    @Transactional
    void fullUpdateAbonosWithPatch() throws Exception {
        // Initialize the database
        abonosRepository.saveAndFlush(abonos);

        int databaseSizeBeforeUpdate = abonosRepository.findAll().size();

        // Update the abonos using partial update
        Abonos partialUpdatedAbonos = new Abonos();
        partialUpdatedAbonos.setId(abonos.getId());

        partialUpdatedAbonos.saldoAnterior(UPDATED_SALDO_ANTERIOR).abono(UPDATED_ABONO).nuevoSaldo(UPDATED_NUEVO_SALDO);

        restAbonosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbonos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbonos))
            )
            .andExpect(status().isOk());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeUpdate);
        Abonos testAbonos = abonosList.get(abonosList.size() - 1);
        assertThat(testAbonos.getSaldoAnterior()).isEqualTo(UPDATED_SALDO_ANTERIOR);
        assertThat(testAbonos.getAbono()).isEqualTo(UPDATED_ABONO);
        assertThat(testAbonos.getNuevoSaldo()).isEqualTo(UPDATED_NUEVO_SALDO);
    }

    @Test
    @Transactional
    void patchNonExistingAbonos() throws Exception {
        int databaseSizeBeforeUpdate = abonosRepository.findAll().size();
        abonos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, abonos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(abonos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAbonos() throws Exception {
        int databaseSizeBeforeUpdate = abonosRepository.findAll().size();
        abonos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(abonos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAbonos() throws Exception {
        int databaseSizeBeforeUpdate = abonosRepository.findAll().size();
        abonos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(abonos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Abonos in the database
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAbonos() throws Exception {
        // Initialize the database
        abonosRepository.saveAndFlush(abonos);

        int databaseSizeBeforeDelete = abonosRepository.findAll().size();

        // Delete the abonos
        restAbonosMockMvc
            .perform(delete(ENTITY_API_URL_ID, abonos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Abonos> abonosList = abonosRepository.findAll();
        assertThat(abonosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
