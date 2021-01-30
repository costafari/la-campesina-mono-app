package com.mkp.lacampesina.repository;

import com.mkp.lacampesina.domain.Proveedores;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Proveedores entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProveedoresRepository extends JpaRepository<Proveedores, Long> {
}
