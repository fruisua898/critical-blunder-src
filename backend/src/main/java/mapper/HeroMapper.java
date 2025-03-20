package mapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.criticalblunder.dto.request.HeroRequestDTO;
import com.criticalblunder.dto.response.HeroResponseDTO;
import com.criticalblunder.model.Hero;

@Mapper(componentModel = "spring")
public interface HeroMapper {

    @Mapping(target = "userName", ignore = true)
    HeroResponseDTO toResponseDTO(Hero Hero);

    Hero toEntity(HeroRequestDTO HeroRequestDTO);

    @AfterMapping
    default void mapUserName(@MappingTarget HeroResponseDTO dto, Hero hero) {
        if (hero.getUser() != null) {
            dto.setUserName(hero.getUser().getName());
        }
    }
}
