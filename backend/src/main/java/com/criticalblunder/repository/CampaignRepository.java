package com.criticalblunder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.User;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByGameMasterId(Long gameMasterId);
    Optional<Campaign> findByName(String name);
    Optional<Campaign> findById(Long id);
    void deleteById(Long id);
    @Query("SELECT COUNT(c) > 0 FROM Campaign c WHERE c.name = :name AND c.gameMaster = :gameMaster")
    boolean existsByNameAndGameMaster(@Param("name") String name, @Param("gameMaster") User gameMaster);
    @Query("SELECT c FROM Campaign c " +
            "JOIN HeroCampaign hc ON c.id = hc.campaign.id " +
            "WHERE hc.hero.id = :heroId")
     List<Campaign> findCampaignsByHeroId(@Param("heroId") Long heroId);
}


