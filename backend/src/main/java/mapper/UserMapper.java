package mapper;

import org.mapstruct.Mapper;

import com.criticalblunder.dto.request.UserRequestDTO;
import com.criticalblunder.dto.response.UserResponseDTO;
import com.criticalblunder.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponseDTO(User user);
    User toEntity(UserRequestDTO userRequestDTO);
}