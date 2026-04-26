package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.UserDto;

import java.security.Key;

public interface JwtService {

    UserDto login(UserDto userDto);
    String generateToken(String userName);

    void validateToken(final String token);

    Key getSignKey();
}
