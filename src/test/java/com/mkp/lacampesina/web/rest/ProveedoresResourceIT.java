package com.mkp.lacampesina.web.rest;

import com.mkp.lacampesina.LacampesinaApp;
import com.mkp.lacampesina.domain.Proveedores;
import com.mkp.lacampesina.repository.ProveedoresRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProveedoresResource} REST controller.
 */
@SpringBootTest(classes = LacampesinaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProveedoresResourceIT {

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_CONTACTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CONTACTO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_EMPRESA = "BBBBBBBBBB";

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String DEFAULT_SITIO_WEB = "AAAAAAAAAA";
    private static final String UPDATED_SITIO_WEB = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_FIJO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_FIJO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_FIJO_2 = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_FIJO_2 = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_MOVIL = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_MOVIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_MOVIL_2 = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_MOVIL_2 = "BBBBBBBBBB";

    @Autowired
    private ProveedoresRepository proveedoresRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProveedoresMockMvc;

    private Proveedores proveedores;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proveedores createEntity(EntityManager em) {
        Proveedores proveedores = new Proveedores()
            .createdAt(DEFAULT_CREATED_AT)
            .direccion(DEFAULT_DIRECCION)
            .nombreContacto(DEFAULT_NOMBRE_CONTACTO)
            .nombreEmpresa(DEFAULT_NOMBRE_EMPRESA)
            .notas(DEFAULT_NOTAS)
            .sitioWeb(DEFAULT_SITIO_WEB)
            .telefonoFijo(DEFAULT_TELEFONO_FIJO)
            .telefonoFijo2(DEFAULT_TELEFONO_FIJO_2)
            .telefonoMovil(DEFAULT_TELEFONO_MOVIL)
            .telefonoMovil2(DEFAULT_TELEFONO_MOVIL_2);
        return proveedores;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proveedores createUpdatedEntity(EntityManager em) {
        Proveedores proveedores = new Proveedores()
            .createdAt(UPDATED_CREATED_AT)
            .direccion(UPDATED_DIRECCION)
            .nombreContacto(UPDATED_NOMBRE_CONTACTO)
            .nombreEmpresa(UPDATED_NOMBRE_EMPRESA)
            .notas(UPDATED_NOTAS)
            .sitioWeb(UPDATED_SITIO_WEB)
            .telefonoFijo(UPDATED_TELEFONO_FIJO)
            .telefonoFijo2(UPDATED_TELEFONO_FIJO_2)
            .telefonoMovil(UPDATED_TELEFONO_MOVIL)
            .telefonoMovil2(UPDATED_TELEFONO_MOVIL_2);
        return proveedores;
    }

    @BeforeEach
    public void initTest() {
        proveedores = createEntity(em);
    }

    @Test
    @Transactional
    public void createProveedores() throws Exception {
        int databaseSizeBeforeCreate = proveedoresRepository.findAll().size();
        // Create the Proveedores
        restProveedoresMockMvc.perform(post("/api/proveedores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proveedores)))
            .andExpect(status().isCreated());

        // Validate the Proveedores in the database
        List<Proveedores> proveedoresList = proveedoresRepository.findAll();
        assertThat(proveedoresList).hasSize(databaseSizeBeforeCreate + 1);
        Proveedores testProveedores = proveedoresList.get(proveedoresList.size() - 1);
        assertThat(testProveedores.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProveedores.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testProveedores.getNombreContacto()).isEqualTo(DEFAULT_NOMBRE_CONTACTO);
        assertThat(testProveedores.getNombreEmpresa()).isEqualTo(DEFAULT_NOMBRE_EMPRESA);
        assertThat(testProveedores.getNotas()).isEqualTo(DEFAULT_NOTAS);
        assertThat(testProveedores.getSitioWeb()).isEqualTo(DEFAULT_SITIO_WEB);
        assertThat(testProveedores.getTelefonoFijo()).isEqualTo(DEFAULT_TELEFONO_FIJO);
        assertThat(testProveedores.getTelefonoFijo2()).isEqualTo(DEFAULT_TELEFONO_FIJO_2);
        assertThat(testProveedores.getTelefonoMovil()).isEqualTo(DEFAULT_TELEFONO_MOVIL);
        assertThat(testProveedores.getTelefonoMovil2()).isEqualTo(DEFAULT_TELEFONO_MOVIL_2);
    }

    @Test
    @Transactional
    public void createProveedoresWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proveedoresRepository.findAll().size();

        // Create the Proveedores with an existing ID
        proveedores.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProveedoresMockMvc.perform(post("/api/proveedores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proveedores)))
            .andExpect(status().isBadRequest());

        // Validate the Proveedores in the database
        List<Proveedores> proveedoresList = proveedoresRepository.findAll();
        assertThat(proveedoresList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProveedores() throws Exception {
        // Initialize the database
        proveedoresRepository.saveAndFlush(proveedores);

        // Get all the proveedoresList
        restProveedoresMockMvc.perform(get("/api/proveedores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proveedores.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].nombreContacto").value(hasItem(DEFAULT_NOMBRE_CONTACTO)))
            .andExpect(jsonPath("$.[*].nombreEmpresa").value(hasItem(DEFAULT_NOMBRE_EMPRESA)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)))
            .andExpect(jsonPath("$.[*].sitioWeb").value(hasItem(DEFAULT_SITIO_WEB)))
            .andExpect(jsonPath("$.[*].telefonoFijo").value(hasItem(DEFAULT_TELEFONO_FIJO)))
            .andExpect(jsonPath("$.[*].telefonoFijo2").value(hasItem(DEFAULT_TELEFONO_FIJO_2)))
            .andExpect(jsonPath("$.[*].telefonoMovil").value(hasItem(DEFAULT_TELEFONO_MOVIL)))
            .andExpect(jsonPath("$.[*].telefonoMovil2").value(hasItem(DEFAULT_TELEFONO_MOVIL_2)));
    }
    
    @Test
    @Transactional
    public void getProveedores() throws Exception {
        // Initialize the database
        proveedoresRepository.saveAndFlush(proveedores);

        // Get the proveedores
        restProveedoresMockMvc.perform(get("/api/proveedores/{id}", proveedores.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proveedores.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.nombreContacto").value(DEFAULT_NOMBRE_CONTACTO))
            .andExpect(jsonPath("$.nombreEmpresa").value(DEFAULT_NOMBRE_EMPRESA))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS))
            .andExpect(jsonPath("$.sitioWeb").value(DEFAULT_SITIO_WEB))
            .andExpect(jsonPath("$.telefonoFijo").value(DEFAULT_TELEFONO_FIJO))
            .andExpect(jsonPath("$.telefonoFijo2").value(DEFAULT_TELEFONO_FIJO_2))
            .andExpect(jsonPath("$.telefonoMovil").value(DEFAULT_TELEFONO_MOVIL))
            .andExpect(jsonPath("$.telefonoMovil2").value(DEFAULT_TELEFONO_MOVIL_2));
    }
    @Test
    @Transactional
    public void getNonExistingProveedores() throws Exception {
        // Get the proveedores
        restProveedoresMockMvc.perform(get("/api/proveedores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProveedores() throws Exception {
        // Initialize the database
        proveedoresRepository.saveAndFlush(proveedores);

        int databaseSizeBeforeUpdate = proveedoresRepository.findAll().size();

        // Update the proveedores
        Proveedores updatedProveedores = proveedoresRepository.findById(proveedores.getId()).get();
        // Disconnect from session so that the updates on updatedProveedores are not directly saved in db
        em.detach(updatedProveedores);
        updatedProveedores
            .createdAt(UPDATED_CREATED_AT)
            .direccion(UPDATED_DIRECCION)
            .nombreContacto(UPDATED_NOMBRE_CONTACTO)
            .nombreEmpresa(UPDATED_NOMBRE_EMPRESA)
            .notas(UPDATED_NOTAS)
            .sitioWeb(UPDATED_SITIO_WEB)
            .telefonoFijo(UPDATED_TELEFONO_FIJO)
            .telefonoFijo2(UPDATED_TELEFONO_FIJO_2)
            .telefonoMovil(UPDATED_TELEFONO_MOVIL)
            .telefonoMovil2(UPDATED_TELEFONO_MOVIL_2);

        restProveedoresMockMvc.perform(put("/api/proveedores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProveedores)))
            .andExpect(status().isOk());

        // Validate the Proveedores in the database
        List<Proveedores> proveedoresList = proveedoresRepository.findAll();
        assertThat(proveedoresList).hasSize(databaseSizeBeforeUpdate);
        Proveedores testProveedores = proveedoresList.get(proveedoresList.size() - 1);
        assertThat(testProveedores.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProveedores.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testProveedores.getNombreContacto()).isEqualTo(UPDATED_NOMBRE_CONTACTO);
        assertThat(testProveedores.getNombreEmpresa()).isEqualTo(UPDATED_NOMBRE_EMPRESA);
        assertThat(testProveedores.getNotas()).isEqualTo(UPDATED_NOTAS);
        assertThat(testProveedores.getSitioWeb()).isEqualTo(UPDATED_SITIO_WEB);
        assertThat(testProveedores.getTelefonoFijo()).isEqualTo(UPDATED_TELEFONO_FIJO);
        assertThat(testProveedores.getTelefonoFijo2()).isEqualTo(UPDATED_TELEFONO_FIJO_2);
        assertThat(testProveedores.getTelefonoMovil()).isEqualTo(UPDATED_TELEFONO_MOVIL);
        assertThat(testProveedores.getTelefonoMovil2()).isEqualTo(UPDATED_TELEFONO_MOVIL_2);
    }

    @Test
    @Transactional
    public void updateNonExistingProveedores() throws Exception {
        int databaseSizeBeforeUpdate = proveedoresRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProveedoresMockMvc.perform(put("/api/proveedores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proveedores)))
            .andExpect(status().isBadRequest());

        // Validate the Proveedores in the database
        List<Proveedores> proveedoresList = proveedoresRepository.findAll();
        assertThat(proveedoresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProveedores() throws Exception {
        // Initialize the database
        proveedoresRepository.saveAndFlush(proveedores);

        int databaseSizeBeforeDelete = proveedoresRepository.findAll().size();

        // Delete the proveedores
        restProveedoresMockMvc.perform(delete("/api/proveedores/{id}", proveedores.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proveedores> proveedoresList = proveedoresRepository.findAll();
        assertThat(proveedoresList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
