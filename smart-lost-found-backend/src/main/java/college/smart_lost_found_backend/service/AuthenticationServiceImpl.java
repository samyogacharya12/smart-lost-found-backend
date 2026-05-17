package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;


    @Override
    public UserDto login(UserDto userDto) {
        Authentication authenticate = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken
                (userDto.getUserName(), userDto.getPassword()));
        Optional<UserDto> findUser=userService.findByUsername(userDto.getUserName());
        if (authenticate.isAuthenticated() && findUser.isPresent() &&
                findUser.get().getEmailVerified()) {
            String token= jwtService.generateToken(userDto.getUserName());
            findUser.ifPresent(dto -> userDto.setRoles(dto.getRoles()));
            userDto.setPassword(null);
            userDto.setUserName(null);
            userDto.setStatus(true);
            userDto.setDeleted(false);
            userDto.setToken(token);
            return userDto;
        } else {
            throw new RuntimeException("invalid access");
        }
    }
}
