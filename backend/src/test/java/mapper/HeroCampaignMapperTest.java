package mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.criticalblunder.dto.response.HeroCampaignResponseDTO;
import com.criticalblunder.enums.HeroStatusEnum;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.Hero;
import com.criticalblunder.model.HeroCampaign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Date;

class HeroCampaignMapperTest {

    private final HeroCampaignMapper heroCampaignMapper = Mappers.getMapper(HeroCampaignMapper.class);
    private Hero hero;
    private Campaign campaign;
    private HeroCampaign heroCampaign;

    @BeforeEach
    void setUp() {
        hero = new Hero();
        hero.setId(1L);
        hero.setName("Brave Hero");

        campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("Epic Campaign");

        heroCampaign = new HeroCampaign();
        heroCampaign.setHero(hero);
        heroCampaign.setCampaign(campaign);
        heroCampaign.setLevel(5);
        heroCampaign.setExperience(1000);
        heroCampaign.setStatus(HeroStatusEnum.ALIVE);
        heroCampaign.setAssignedAt(new Date());
    }

    @Test
    void shouldMapHeroCampaignToResponseDTO() {
        HeroCampaignResponseDTO dto = heroCampaignMapper.toResponseDTO(heroCampaign);

        assertNotNull(dto);
        assertEquals(hero.getId(), dto.getHeroId());
        assertEquals(hero.getName(), dto.getHeroName());
        assertEquals(campaign.getId(), dto.getCampaignId());
        assertEquals(campaign.getName(), dto.getCampaignName());
        assertEquals(heroCampaign.getLevel(), dto.getLevel());
        assertEquals(heroCampaign.getExperience(), dto.getExperience());
        assertEquals(heroCampaign.getStatus().toString(), dto.getStatus());
    }
}
