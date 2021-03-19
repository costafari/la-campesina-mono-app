package com.mkp.lacampesina.repository;

import com.mkp.lacampesina.domain.FacturasDetalle;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FacturasDetalle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturasDetalleRepository extends JpaRepository<FacturasDetalle, Long> {
}
