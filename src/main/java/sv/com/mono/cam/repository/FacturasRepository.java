package sv.com.mono.cam.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sv.com.mono.cam.domain.Facturas;

/**
 * Spring Data SQL repository for the Facturas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturasRepository extends JpaRepository<Facturas, Long> {}
