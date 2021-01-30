package com.mkp.lacampesina.repository;

import com.mkp.lacampesina.domain.Tickets;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Tickets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketsRepository extends JpaRepository<Tickets, Long> {
}
