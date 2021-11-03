package sv.com.mono.cam.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sv.com.mono.cam.domain.Clientes;

/**
 * Spring Data SQL repository for the Clientes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientesRepository extends JpaRepository<Clientes, Long> {}
