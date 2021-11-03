package sv.com.mono.cam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.mono.cam.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
