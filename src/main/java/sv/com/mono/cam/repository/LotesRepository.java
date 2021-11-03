package sv.com.mono.cam.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sv.com.mono.cam.domain.Lotes;

/**
 * Spring Data SQL repository for the Lotes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LotesRepository extends JpaRepository<Lotes, Long> {}
