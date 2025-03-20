package com.criticalblunder.service;

import com.criticalblunder.dto.request.EventRequestDTO;
import com.criticalblunder.model.Event;

import java.util.List;

/**
 * Servicio para gestionar los eventos dentro de las campañas.
 * Proporciona métodos para crear, listar, actualizar y eliminar eventos.
 */
public interface EventService {

    /**
     * Crea un nuevo evento en una campaña específica.
     *
     * @param campaignId ID de la campaña donde se creará el evento.
     * @param request DTO con los datos del evento a crear.
     * @param gmEmail Email del Game Master que realiza la operación.
     * @return El evento creado.
     */
    Event createEvent(Long campaignId, EventRequestDTO request, String gmEmail);

    /**
     * Obtiene una lista de todos los eventos de una campaña específica.
     *
     * @param campaignId ID de la campaña cuyos eventos se desean listar.
     * @return Lista de eventos asociados a la campaña.
     */
    List<Event> getEventsByCampaign(Long campaignId);

    /**
     * Actualiza un evento existente.
     *
     * @param eventId ID del evento que se desea actualizar.
     * @param request DTO con los nuevos datos del evento.
     * @param gmEmail Email del Game Master que realiza la operación.
     * @return El evento actualizado.
     */
    Event updateEvent(Long eventId, EventRequestDTO request, String gmEmail);

    /**
     * Elimina un evento específico.
     *
     * @param eventId ID del evento que se desea eliminar.
     * @param gmEmail Email del Game Master que realiza la operación.
     */
    void deleteEvent(Long eventId, String gmEmail);
}
