package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.UserDto;

public interface AuthenticationService {

    UserDto login(UserDto userDto);

}
