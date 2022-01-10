package sv.com.mono.cam.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.com.mono.cam.domain.Detalles;
import sv.com.mono.cam.repository.DetallesRepository;

/**
 * Service Implementation for managing {@link Detalles}.
 */
@Service
@Transactional
public class DetallesService {

    private final Logger log = LoggerFactory.getLogger(DetallesService.class);

    private final DetallesRepository detallesRepository;

    public DetallesService(DetallesRepository detallesRepository) {
        this.detallesRepository = detallesRepository;
    }

    /**
     * Save a detalles.
     *
     * @param detalles the entity to save.
     * @return the persisted entity.
     */
    public Detalles save(Detalles detalles) {
        log.debug("Request to save Detalles : {}", detalles);
        return detallesRepository.save(detalles);
    }

    /**
     * Partially update a detalles.
     *
     * @param detalles the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Detalles> partialUpdate(Detalles detalles) {
        log.debug("Request to partially update Detalles : {}", detalles);

        return detallesRepository
            .findById(detalles.getId())
            .map(
                existingDetalles -> {
                    if (detalles.getCantidad() != null) {
                        existingDetalles.setCantidad(detalles.getCantidad());
                    }
                    if (detalles.getTotal() != null) {
                        existingDetalles.setTotal(detalles.getTotal());
                    }

                    return existingDetalles;
                }
            )
            .map(detallesRepository::save);
    }

    /**
     * Get all the detalles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Detalles> findAll() {
        log.debug("Request to get all Detalles");
        return detallesRepository.findAll();
    }

    /**
     * Get one detalles by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Detalles> findOne(Long id) {
        log.debug("Request to get Detalles : {}", id);
        return detallesRepository.findById(id);
    }

    /**
     * Delete the detalles by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Detalles : {}", id);
        detallesRepository.deleteById(id);
    }
}
