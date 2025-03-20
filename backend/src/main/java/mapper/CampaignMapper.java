package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.criticalblunder.dto.request.CampaignRequestDTO;
import com.criticalblunder.dto.response.CampaignResponseDTO;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.User;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CampaignMapper {

    @Mapping(source = "gameMaster", target = "gameMasterName", qualifiedByName = "mapUserName")
    CampaignResponseDTO toResponseDTO(Campaign campaign);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gameMaster", ignore = true)
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    Campaign toEntity(CampaignRequestDTO campaignRequestDTO);

    @Named("mapUserName")
    default String mapUserName(User user) {
        return user != null ? user.getName() : null;
    }
    
    
}
