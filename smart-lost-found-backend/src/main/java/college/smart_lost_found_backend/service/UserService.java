package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.RestResponse;
import college.smart_lost_found_backend.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {

    RestResponse save(UserDto userDto);

    RestResponse update(UserDto userDto);

    void forgotPassword(String email);

    RestResponse resetPassword(String token, String newPassword);

    RestResponse verifyEmail(String token);


    RestResponse findAll();


    UserDto findByUserId(Long userId);

    Optional<UserDto> findByUsername(String username);

    UserDetails loadUserByUsername(String username);
}
