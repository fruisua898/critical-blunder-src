package mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.criticalblunder.dto.request.HeroRequestDTO;
import com.criticalblunder.dto.response.HeroResponseDTO;
import com.criticalblunder.enums.HeroClassEnum;
import com.criticalblunder.model.Hero;
import com.criticalblunder.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class HeroMapperTest {

    private final HeroMapper heroMapper = Mappers.getMapper(HeroMapper.class);
    private Hero hero;
    private HeroRequestDTO heroRequestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");

        hero = new Hero();
        hero.setId(1L);
        hero.setName("Test Hero");
        hero.setUser(user);
        hero.setDescription("Hero description");
        hero.setAge(25);
        hero.setAppearance("Tall and strong");
        hero.setHeroClass(HeroClassEnum.BARBARIAN);

        heroRequestDTO = new HeroRequestDTO("New Hero", HeroClassEnum.BARBARIAN, "WARRIOR", 18, "Appearance");
    }

    @Test
    void shouldMapHeroToResponseDTO() {
        HeroResponseDTO dto = heroMapper.toResponseDTO(hero);

        assertNotNull(dto);
        assertEquals(hero.getName(), dto.getName());
        assertEquals(hero.getDescription(), dto.getDescription());
        assertEquals(hero.getAge(), dto.getAge());
        assertEquals(hero.getAppearance(), dto.getAppearance());
        assertEquals(hero.getHeroClass().name(), dto.getHeroClass());
        assertEquals(user.getName(), dto.getUserName());
    }

    @Test
    void shouldMapHeroRequestDTOToEntity() {
        Hero entity = heroMapper.toEntity(heroRequestDTO);

        assertNotNull(entity);
        assertEquals(heroRequestDTO.getName(), entity.getName());
        assertEquals(heroRequestDTO.getDescription(), entity.getDescription());
        assertEquals(heroRequestDTO.getAge(), entity.getAge());
        assertEquals(heroRequestDTO.getAppearance(), entity.getAppearance());
        assertEquals(heroRequestDTO.getHeroClass(), entity.getHeroClass());
    }
}
