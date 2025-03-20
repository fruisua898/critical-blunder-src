package mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.criticalblunder.dto.request.UserRequestDTO;
import com.criticalblunder.dto.response.UserResponseDTO;
import com.criticalblunder.enums.RoleEnum;
import com.criticalblunder.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private User user;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setRole(RoleEnum.PLAYER);

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Jane Doe");
        userRequestDTO.setEmail("jane@example.com");
        userRequestDTO.setPassword("securepassword");
    }

    @Test
    void shouldMapUserToResponseDTO() {
        UserResponseDTO dto = userMapper.toResponseDTO(user);

        assertNotNull(dto);
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void shouldMapUserRequestDTOToEntity() {
        User entity = userMapper.toEntity(userRequestDTO);

        assertNotNull(entity);
        assertEquals(userRequestDTO.getName(), entity.getName());
        assertEquals(userRequestDTO.getEmail(), entity.getEmail());
        assertEquals(userRequestDTO.getPassword(), entity.getPassword());
    }
}
