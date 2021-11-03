package sv.com.mono.cam.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sv.com.mono.cam.domain.Precios;

/**
 * Spring Data SQL repository for the Precios entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreciosRepository extends JpaRepository<Precios, Long> {}
