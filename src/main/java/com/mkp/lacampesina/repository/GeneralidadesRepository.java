package com.mkp.lacampesina.repository;

import com.mkp.lacampesina.domain.Generalidades;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Generalidades entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneralidadesRepository extends JpaRepository<Generalidades, Long> {
}
