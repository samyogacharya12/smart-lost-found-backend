package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.UserDao;
import college.smart_lost_found_backend.dto.UserDto;
import college.smart_lost_found_backend.dto.UserInfoDetails;
import college.smart_lost_found_backend.mapper.UserMapper;
import college.smart_lost_found_backend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {


     @Autowired
     private UserDao userDao;

     @Autowired
     private UserMapper userMapper;


    @Autowired
    private JwtService jwtService;

    @Override
    public String save(UserDto userDto) {
        log.info("UserServiceImpl save userDto ");
        int value=userDao.save(userMapper.toEntity(userDto));
        if(value>0){
            return "user saved successfully";
        }
        return "user could not be saved";
    }


    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userDao.findByName(username)
                .map(user -> userMapper.toDto(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInfo = userDao.findByName(username);
        return userInfo.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }
}
