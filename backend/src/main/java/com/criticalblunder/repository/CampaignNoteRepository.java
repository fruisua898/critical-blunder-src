package com.criticalblunder.repository;

import com.criticalblunder.model.CampaignNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para gestionar las notas de campaña en la base de datos.
 * Extiende JpaRepository para proporcionar operaciones CRUD.
 */
public interface CampaignNoteRepository extends JpaRepository<CampaignNote, Long> {

    /**
     * Encuentra todas las notas asociadas a una campaña específica.
     *
     * @param campaignId ID de la campaña cuyas notas se desean obtener.
     * @return Lista de notas asociadas a la campaña.
     */
    List<CampaignNote> findByCampaignId(Long campaignId);
}
