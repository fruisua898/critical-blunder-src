package com.criticalblunder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.criticalblunder.dto.request.CampaignRequestDTO;
import com.criticalblunder.dto.response.CampaignResponseDTO;
import com.criticalblunder.dto.response.HeroCampaignResponseDTO;
import com.criticalblunder.dto.response.HeroResponseDTO;
import com.criticalblunder.enums.CampaignStatusEnum;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.User;

@Service
public interface CampaignService {

	public Campaign createCampaign(String title, String description, User user);

	public List<CampaignResponseDTO> getUserCampaigns(Long id);

	List<CampaignResponseDTO> getUserActiveCampaigns(Long id);

	void deleteCampaign(Long campaignId, User user);

	Optional<CampaignResponseDTO> findByName(String name);

	Optional<CampaignResponseDTO> findById(Long id);

	List<CampaignResponseDTO> getAllCampaigns();

	void updateCampaignStatus(Long campaignId, CampaignStatusEnum newStatus, User user);

	Campaign updateCampaignDetails(Long id, CampaignRequestDTO request, User authenticatedUser);

	public void addHeroToCampaign(Long campaignId, Long heroId, User gm);

	void validateGameMaster(User gm, Long campaignId);
	
	List<CampaignResponseDTO> getCampaignsWhereUserIsParticipant(Long userId);

	
	 /**
     * Obtiene la lista de héroes asignados a la campaña especificada.
     *
     * Se verifica que el usuario autenticado (Game Master) sea el propietario de la campaña.
     *
     * @param campaignId el ID de la campaña.
     * @param gm el usuario autenticado (Game Master).
     * @return lista de héroes asignados a la campaña.
     * @throws RuntimeException si la campaña no existe o si el usuario no tiene permiso.
     */
    List<HeroCampaignResponseDTO> getAssignedHeroes(Long campaignId, User gm);
}