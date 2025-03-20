package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.criticalblunder.dto.response.HeroCampaignResponseDTO;
import com.criticalblunder.model.HeroCampaign;

@Mapper(componentModel = "spring", uses = { HeroMapper.class, CampaignMapper.class })
public interface HeroCampaignMapper {

    @Mapping(source = "hero.id", target = "heroId")
    @Mapping(source = "campaign.id", target = "campaignId")
    @Mapping(source = "hero.name", target = "heroName")
    @Mapping(source = "campaign.name", target = "campaignName")
    @Mapping(source = "level", target = "level")
    @Mapping(source = "experience", target = "experience")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "assignedAt", target = "assignedAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    HeroCampaignResponseDTO toResponseDTO(HeroCampaign heroCampaign);
}
