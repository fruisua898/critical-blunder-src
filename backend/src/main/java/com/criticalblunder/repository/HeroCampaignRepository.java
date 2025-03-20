package com.criticalblunder.repository;

import com.criticalblunder.model.HeroCampaign;
import com.criticalblunder.model.HeroCampaignId;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroCampaignRepository extends JpaRepository<HeroCampaign, HeroCampaignId> {

	@Query("SELECT COUNT(hc) > 0 FROM HeroCampaign hc "
			+ "WHERE hc.hero.user.id = :userId AND hc.campaign.id = :campaignId")
	boolean existsByUserIdAndCampaignId(@Param("userId") Long userId, @Param("campaignId") Long campaignId);

	boolean existsByHeroIdAndCampaignId(Long heroId, Long campaignId);

	boolean existsByHeroId(Long heroId);

	Optional<HeroCampaign> findByHeroIdAndCampaignId(Long heroId, Long campaignId);
	

	/**
	 * 
	 * Devuelve los héroes asignados a la campaña con el ID especificado.
	 *
	 * @param campaignId el ID de la campaña.
	 * @return lista de héroes asignados.
	 */
    @Query("SELECT hc FROM HeroCampaign hc WHERE hc.campaign.id = :campaignId")
    List<HeroCampaign> findByCampaignId(@Param("campaignId") Long campaignId);
}