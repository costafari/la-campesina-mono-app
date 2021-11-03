package sv.com.mono.cam.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sv.com.mono.cam.domain.Productos;

/**
 * Spring Data SQL repository for the Productos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductosRepository extends JpaRepository<Productos, Long> {}
