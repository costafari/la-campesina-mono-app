package com.mkp.lacampesina.repository;

import com.mkp.lacampesina.domain.AbonoFacturas;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AbonoFacturas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonoFacturasRepository extends JpaRepository<AbonoFacturas, Long> {}
