package com.criticalblunder.service;

import com.criticalblunder.dto.request.CampaignNoteRequestDTO;
import com.criticalblunder.model.CampaignNote;

import java.util.List;

/**
 * Servicio para gestionar las notas dentro de las campañas.
 * Proporciona métodos para crear, listar, actualizar y eliminar notas.
 */
public interface CampaignNoteService {

    /**
     * Crea una nueva nota en una campaña específica.
     *
     * @param campaignId ID de la campaña donde se creará la nota.
     * @param request DTO con los datos de la nota a crear.
     * @param userEmail Email del usuario que realiza la operación.
     * @return La nota creada.
     */
    CampaignNote createNote(Long campaignId, CampaignNoteRequestDTO request, String userEmail);

    /**
     * Obtiene una lista de todas las notas de una campaña específica.
     *
     * @param campaignId ID de la campaña cuyas notas se desean listar.
     * @return Lista de notas asociadas a la campaña.
     */
    List<CampaignNote> getNotesByCampaign(Long campaignId);

    /**
     * Actualiza una nota existente.
     *
     * @param noteId ID de la nota que se desea actualizar.
     * @param request DTO con los nuevos datos de la nota.
     * @param gmEmail Email del usuario que realiza la operación.
     * @return La nota actualizada.
     */
    CampaignNote updateNote(Long noteId, CampaignNoteRequestDTO request, String gmEmail);

    /**
     * Elimina una nota específica.
     *
     * @param noteId ID de la nota que se desea eliminar.
     * @param gmEmail Email del usuario que realiza la operación.
     */
    void deleteNote(Long noteId, String gmEmail);
}
