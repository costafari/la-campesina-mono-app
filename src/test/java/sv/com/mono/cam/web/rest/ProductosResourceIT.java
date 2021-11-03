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
import sv.com.mono.cam.domain.Productos;
import sv.com.mono.cam.repository.ProductosRepository;

/**
 * Integration tests for the {@link ProductosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductosResourceIT {

    private static final String DEFAULT_DESCIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductosMockMvc;

    private Productos productos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Productos createEntity(EntityManager em) {
        Productos productos = new Productos().descipcion(DEFAULT_DESCIPCION).nombre(DEFAULT_NOMBRE).notas(DEFAULT_NOTAS);
        return productos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Productos createUpdatedEntity(EntityManager em) {
        Productos productos = new Productos().descipcion(UPDATED_DESCIPCION).nombre(UPDATED_NOMBRE).notas(UPDATED_NOTAS);
        return productos;
    }

    @BeforeEach
    public void initTest() {
        productos = createEntity(em);
    }

    @Test
    @Transactional
    void createProductos() throws Exception {
        int databaseSizeBeforeCreate = productosRepository.findAll().size();
        // Create the Productos
        restProductosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productos)))
            .andExpect(status().isCreated());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeCreate + 1);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getDescipcion()).isEqualTo(DEFAULT_DESCIPCION);
        assertThat(testProductos.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProductos.getNotas()).isEqualTo(DEFAULT_NOTAS);
    }

    @Test
    @Transactional
    void createProductosWithExistingId() throws Exception {
        // Create the Productos with an existing ID
        productos.setId(1L);

        int databaseSizeBeforeCreate = productosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productos)))
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList
        restProductosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productos.getId().intValue())))
            .andExpect(jsonPath("$.[*].descipcion").value(hasItem(DEFAULT_DESCIPCION)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));
    }

    @Test
    @Transactional
    void getProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get the productos
        restProductosMockMvc
            .perform(get(ENTITY_API_URL_ID, productos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productos.getId().intValue()))
            .andExpect(jsonPath("$.descipcion").value(DEFAULT_DESCIPCION))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS));
    }

    @Test
    @Transactional
    void getNonExistingProductos() throws Exception {
        // Get the productos
        restProductosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeUpdate = productosRepository.findAll().size();

        // Update the productos
        Productos updatedProductos = productosRepository.findById(productos.getId()).get();
        // Disconnect from session so that the updates on updatedProductos are not directly saved in db
        em.detach(updatedProductos);
        updatedProductos.descipcion(UPDATED_DESCIPCION).nombre(UPDATED_NOMBRE).notas(UPDATED_NOTAS);

        restProductosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductos))
            )
            .andExpect(status().isOk());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getDescipcion()).isEqualTo(UPDATED_DESCIPCION);
        assertThat(testProductos.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProductos.getNotas()).isEqualTo(UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void putNonExistingProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductosWithPatch() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeUpdate = productosRepository.findAll().size();

        // Update the productos using partial update
        Productos partialUpdatedProductos = new Productos();
        partialUpdatedProductos.setId(productos.getId());

        partialUpdatedProductos.descipcion(UPDATED_DESCIPCION);

        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductos))
            )
            .andExpect(status().isOk());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getDescipcion()).isEqualTo(UPDATED_DESCIPCION);
        assertThat(testProductos.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProductos.getNotas()).isEqualTo(DEFAULT_NOTAS);
    }

    @Test
    @Transactional
    void fullUpdateProductosWithPatch() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeUpdate = productosRepository.findAll().size();

        // Update the productos using partial update
        Productos partialUpdatedProductos = new Productos();
        partialUpdatedProductos.setId(productos.getId());

        partialUpdatedProductos.descipcion(UPDATED_DESCIPCION).nombre(UPDATED_NOMBRE).notas(UPDATED_NOTAS);

        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductos))
            )
            .andExpect(status().isOk());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getDescipcion()).isEqualTo(UPDATED_DESCIPCION);
        assertThat(testProductos.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProductos.getNotas()).isEqualTo(UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void patchNonExistingProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productos))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeDelete = productosRepository.findAll().size();

        // Delete the productos
        restProductosMockMvc
            .perform(delete(ENTITY_API_URL_ID, productos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
