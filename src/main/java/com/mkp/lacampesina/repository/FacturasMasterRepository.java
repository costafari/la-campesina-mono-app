package com.mkp.lacampesina.repository;

import com.mkp.lacampesina.domain.FacturasMaster;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FacturasMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturasMasterRepository extends JpaRepository<FacturasMaster, Long> {
}
