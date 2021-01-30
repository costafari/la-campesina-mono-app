package com.mkp.lacampesina.repository;

import com.mkp.lacampesina.domain.Precios;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Precios entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreciosRepository extends JpaRepository<Precios, Long> {
}
