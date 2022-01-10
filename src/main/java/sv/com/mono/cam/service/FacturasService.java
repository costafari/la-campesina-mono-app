package sv.com.mono.cam.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.com.mono.cam.domain.Facturas;
import sv.com.mono.cam.repository.FacturasRepository;

/**
 * Service Implementation for managing {@link Facturas}.
 */
@Service
@Transactional
public class FacturasService {

    private final Logger log = LoggerFactory.getLogger(FacturasService.class);

    private final FacturasRepository facturasRepository;

    public FacturasService(FacturasRepository facturasRepository) {
        this.facturasRepository = facturasRepository;
    }

    /**
     * Save a facturas.
     *
     * @param facturas the entity to save.
     * @return the persisted entity.
     */
    public Facturas save(Facturas facturas) {
        log.debug("Request to save Facturas : {}", facturas);
        return facturasRepository.save(facturas);
    }

    /**
     * Partially update a facturas.
     *
     * @param facturas the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Facturas> partialUpdate(Facturas facturas) {
        log.debug("Request to partially update Facturas : {}", facturas);

        return facturasRepository
            .findById(facturas.getId())
            .map(
                existingFacturas -> {
                    if (facturas.getNumeroFactura() != null) {
                        existingFacturas.setNumeroFactura(facturas.getNumeroFactura());
                    }
                    if (facturas.getFechaFactura() != null) {
                        existingFacturas.setFechaFactura(facturas.getFechaFactura());
                    }
                    if (facturas.getCondicionPago() != null) {
                        existingFacturas.setCondicionPago(facturas.getCondicionPago());
                    }

                    return existingFacturas;
                }
            )
            .map(facturasRepository::save);
    }

    /**
     * Get all the facturas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Facturas> findAll() {
        log.debug("Request to get all Facturas");
        return facturasRepository.findAll();
    }

    /**
     * Get one facturas by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Facturas> findOne(Long id) {
        log.debug("Request to get Facturas : {}", id);
        return facturasRepository.findById(id);
    }

    /**
     * Delete the facturas by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Facturas : {}", id);
        facturasRepository.deleteById(id);
    }
}
