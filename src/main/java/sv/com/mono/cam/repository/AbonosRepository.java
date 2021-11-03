package sv.com.mono.cam.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sv.com.mono.cam.domain.Abonos;

/**
 * Spring Data SQL repository for the Abonos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonosRepository extends JpaRepository<Abonos, Long> {}
