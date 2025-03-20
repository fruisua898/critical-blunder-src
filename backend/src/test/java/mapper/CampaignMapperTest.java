package mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.criticalblunder.dto.request.CampaignRequestDTO;
import com.criticalblunder.dto.response.CampaignResponseDTO;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Date;

class CampaignMapperTest {

    private final CampaignMapper campaignMapper = Mappers.getMapper(CampaignMapper.class);
    private Campaign campaign;
    private CampaignRequestDTO campaignRequestDTO;
    private User gameMaster;

    @BeforeEach
    void setUp() {
        gameMaster = new User();
        gameMaster.setId(1L);
        gameMaster.setName("Game Master");

        campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("Epic Quest");
        campaign.setDescription("A grand adventure");
        campaign.setCreatedAt(new Date());
        campaign.setGameMaster(gameMaster);

        campaignRequestDTO = new CampaignRequestDTO("New Campaign", "A new adventure");
    }

    @Test
    void shouldMapCampaignToResponseDTO() {
        CampaignResponseDTO dto = campaignMapper.toResponseDTO(campaign);

        assertNotNull(dto);
        assertEquals(campaign.getName(), dto.getName());
        assertEquals(gameMaster.getName(), dto.getGameMasterName());
    }

    @Test
    void shouldMapCampaignRequestDTOToEntity() {
        Campaign entity = campaignMapper.toEntity(campaignRequestDTO);

        assertNotNull(entity);
        assertEquals(campaignRequestDTO.getName(), entity.getName());
        assertEquals(campaignRequestDTO.getDescription(), entity.getDescription());
        assertNotNull(entity.getCreatedAt());
    }
}