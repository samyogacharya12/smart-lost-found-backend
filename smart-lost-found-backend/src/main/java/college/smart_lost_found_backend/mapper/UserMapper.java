package college.smart_lost_found_backend.mapper;

import college.smart_lost_found_backend.dto.UserDto;
import college.smart_lost_found_backend.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {


    // Convert Entity → DTO (basic info)
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    // Convert DTO → Entity
    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUserId(dto.getId());
        user.setUsername(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRoles());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(dto.getPassword());
        return user;
    }
}
