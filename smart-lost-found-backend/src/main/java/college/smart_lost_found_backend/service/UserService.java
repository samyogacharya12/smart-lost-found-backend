package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {

    String save(UserDto userDto);

    Optional<UserDto> findByUsername(String username);

    UserDetails loadUserByUsername(String username);
}
