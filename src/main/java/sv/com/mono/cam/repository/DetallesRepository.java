package sv.com.mono.cam.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sv.com.mono.cam.domain.Detalles;

/**
 * Spring Data SQL repository for the Detalles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetallesRepository extends JpaRepository<Detalles, Long>, JpaSpecificationExecutor<Detalles> {}
