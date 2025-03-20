package com.criticalblunder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.criticalblunder.dto.request.HeroCampaignRequestDTO;
import com.criticalblunder.dto.request.HeroRequestDTO;
import com.criticalblunder.dto.request.HeroRequestUpdateDTO;
import com.criticalblunder.dto.response.HeroCampaignResponseDTO;
import com.criticalblunder.dto.response.HeroResponseDTO;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.Hero;
import com.criticalblunder.model.User;

@Service
public interface HeroService {
	public void createHero(HeroRequestDTO request, User user);

	public Hero findHeroByUserEmail(String email);

	public void deleteHero(Long heroId, User user);

	public List<Campaign> getCampaignsForHero(Long heroId);

	public void leaveCampaign(Long heroId, Long campaignId, User user);

	public void kickHeroFromCampaign(Long heroId, Long campaignId);

	void updateHero(Long heroId, HeroRequestUpdateDTO request, User gm);

	void updateHeroCampaign(Long heroId, Long campaignId, HeroCampaignRequestDTO request);

	void validateHeroOwnership(Long heroId, User player);

	public List<HeroResponseDTO> getUserHeroes(User user);

	/**
	 * Obtiene la información de un héroe en forma de DTO.
	 *
	 * @param heroId el identificador del héroe
	 * @return un {@code HeroResponseDTO} con los detalles del héroe
	 * @throws RuntimeException si el héroe no es encontrado
	 */
	HeroResponseDTO getHeroResponse(Long heroId);

	List<HeroResponseDTO> getHeroesByUserId(Long userId);


    /**
     * Busca héroes basándose en el nombre (o parte del nombre) del usuario propietario.
     *
     * @param ownerName el nombre o parte del nombre del usuario.
     * @return lista de {@link HeroResponseDTO} correspondientes a los héroes encontrados.
     */
    List<HeroResponseDTO> searchHeroesByName(String ownerName);

    /**
     * Busca héroes basándose en el nombre (o parte del nombre) del usuario propietario
     * y filtrando por la clase del héroe. Si el parámetro heroClass es "ALL" o nulo,
     * se ignora el filtro por clase.
     *
     * @param ownerName el nombre o parte del nombre del usuario.
     * @param heroClass la clase del héroe a filtrar (por ejemplo, "CLERIC"); o "ALL" para no filtrar.
     * @return lista de {@link HeroResponseDTO} correspondientes a los héroes encontrados.
     */
    List<HeroResponseDTO> searchHeroesByNameAndHeroClass(String ownerName, String heroClass);



}
