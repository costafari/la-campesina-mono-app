package sv.com.mono.cam.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sv.com.mono.cam.domain.Proveedores;

/**
 * Spring Data SQL repository for the Proveedores entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProveedoresRepository extends JpaRepository<Proveedores, Long> {}
