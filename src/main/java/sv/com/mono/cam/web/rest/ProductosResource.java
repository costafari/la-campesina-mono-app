package sv.com.mono.cam.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sv.com.mono.cam.domain.Productos;
import sv.com.mono.cam.repository.ProductosRepository;
import sv.com.mono.cam.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sv.com.mono.cam.domain.Productos}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductosResource {

    private final Logger log = LoggerFactory.getLogger(ProductosResource.class);

    private static final String ENTITY_NAME = "productos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductosRepository productosRepository;

    public ProductosResource(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    /**
     * {@code POST  /productos} : Create a new productos.
     *
     * @param productos the productos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productos, or with status {@code 400 (Bad Request)} if the productos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/productos")
    public ResponseEntity<Productos> createProductos(@RequestBody Productos productos) throws URISyntaxException {
        log.debug("REST request to save Productos : {}", productos);
        if (productos.getId() != null) {
            throw new BadRequestAlertException("A new productos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Productos result = productosRepository.save(productos);
        return ResponseEntity
            .created(new URI("/api/productos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /productos/:id} : Updates an existing productos.
     *
     * @param id the id of the productos to save.
     * @param productos the productos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productos,
     * or with status {@code 400 (Bad Request)} if the productos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/productos/{id}")
    public ResponseEntity<Productos> updateProductos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Productos productos
    ) throws URISyntaxException {
        log.debug("REST request to update Productos : {}, {}", id, productos);
        if (productos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Productos result = productosRepository.save(productos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /productos/:id} : Partial updates given fields of an existing productos, field will ignore if it is null
     *
     * @param id the id of the productos to save.
     * @param productos the productos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productos,
     * or with status {@code 400 (Bad Request)} if the productos is not valid,
     * or with status {@code 404 (Not Found)} if the productos is not found,
     * or with status {@code 500 (Internal Server Error)} if the productos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/productos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Productos> partialUpdateProductos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Productos productos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Productos partially : {}, {}", id, productos);
        if (productos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Productos> result = productosRepository
            .findById(productos.getId())
            .map(
                existingProductos -> {
                    if (productos.getDescipcion() != null) {
                        existingProductos.setDescipcion(productos.getDescipcion());
                    }
                    if (productos.getNombre() != null) {
                        existingProductos.setNombre(productos.getNombre());
                    }
                    if (productos.getNotas() != null) {
                        existingProductos.setNotas(productos.getNotas());
                    }

                    return existingProductos;
                }
            )
            .map(productosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productos.getId().toString())
        );
    }

    /**
     * {@code GET  /productos} : get all the productos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productos in body.
     */
    @GetMapping("/productos")
    public List<Productos> getAllProductos() {
        log.debug("REST request to get all Productos");
        return productosRepository.findAll();
    }

    /**
     * {@code GET  /productos/:id} : get the "id" productos.
     *
     * @param id the id of the productos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<Productos> getProductos(@PathVariable Long id) {
        log.debug("REST request to get Productos : {}", id);
        Optional<Productos> productos = productosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productos);
    }

    /**
     * {@code DELETE  /productos/:id} : delete the "id" productos.
     *
     * @param id the id of the productos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProductos(@PathVariable Long id) {
        log.debug("REST request to delete Productos : {}", id);
        productosRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
