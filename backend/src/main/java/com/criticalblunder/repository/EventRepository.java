package com.criticalblunder.repository;

import com.criticalblunder.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para gestionar los eventos en la base de datos.
 * Extiende JpaRepository para proporcionar operaciones CRUD.
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Encuentra todos los eventos asociados a una campaña específica.
     *
     * @param campaignId ID de la campaña cuyos eventos se desean obtener.
     * @return Lista de eventos asociados a la campaña.
     */
    List<Event> findByCampaignId(Long campaignId);
}
