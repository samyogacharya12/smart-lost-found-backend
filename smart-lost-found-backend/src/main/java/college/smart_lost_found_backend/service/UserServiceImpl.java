package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.UserDao;
import college.smart_lost_found_backend.dto.RestResponse;
import college.smart_lost_found_backend.dto.UserDto;
import college.smart_lost_found_backend.dto.UserInfoDetails;
import college.smart_lost_found_backend.enumconstant.ResponseStatus;
import college.smart_lost_found_backend.exceptions.Invalid;
import college.smart_lost_found_backend.mapper.UserMapper;
import college.smart_lost_found_backend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private EmailService emailService;

    @Override
    public RestResponse save(UserDto userDto) {
        log.info("UserServiceImpl save userDto ");
        RestResponse restResponse = RestResponse.builder().build();
        Optional<User> optionalUser = this.userDao.findByName(userDto.getUserName());
        if (optionalUser.isPresent()) {
            throw new Invalid("Sorry UserName already exist", optionalUser);
        }
        Optional<User> userOptional = this.userDao.findByEmail(userDto.getEmail());
        if (userOptional.isPresent() && Objects.nonNull(userOptional.get().getUserId())) {
            throw new Invalid("Sorry Email already exist", userOptional);
        }
        try {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userDto.setVerificationToken(UUID.randomUUID().toString());
            int value = userDao.save(userMapper.toEntity(userDto));
            String verificationLink =
                    "http://localhost:8080/api/verify?token=" + userDto.getVerificationToken();

            emailService.sendHtmlEmail(
                    userDto.getEmail(),
                    "Verify Your Email - Smart Lost & Found",
                    "Welcome to Smart Lost & Found",
                    """
                            Thank you for registering. Please verify your email using the link below:
                            <br><br>
                            <a href="%s">Verify Email</a>
                            """.formatted(verificationLink)
            );
            if (value > 0) {
                restResponse = RestResponse.builder().build();
                restResponse.setMessage("user " + userDto.getUserName() + "saved successfully");
                restResponse.setResponseStatus(RestResponse.builder().build().getResponseStatus());
                restResponse.setStatus(HttpStatus.ACCEPTED.toString());
                return restResponse;
            }
        } catch (Exception e) {
            log.error("UserServiceImpl save userDto {}", e);
        }
        restResponse.setMessage("User Could not be saved");
        restResponse.setStatus(HttpStatus.ALREADY_REPORTED.toString());
        return restResponse;
    }

    @Override
    public RestResponse verifyEmail(String token) {
        log.info("UserServiceImpl verifyEmail token ");
        RestResponse restResponse = RestResponse.builder().build();
        try {
            User user = userDao.findByVerificationToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid verification token"));

            userDao.verifyEmail(user.getUserId());
            restResponse.setMessage("Email verified successfully");
            restResponse.setResponseStatus(RestResponse.builder().build().getResponseStatus());
            restResponse.setStatus(HttpStatus.ACCEPTED.toString());
            return restResponse;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        restResponse.setMessage("Email did not got verified");
        restResponse.setStatus(HttpStatus.ALREADY_REPORTED.toString());
        return restResponse;
    }

    @Override
    public RestResponse findAll() {
        log.info("UserServiceImpl findAll");
        RestResponse restResponse = RestResponse.builder().build();
        try {
            List<UserDto> userDtoList= userDao.findAll()
                    .stream().map(UserMapper::toDto)
                    .toList();
            restResponse.setDetail(userDtoList);
            restResponse.setResponseStatus(ResponseStatus.SUCCESS);
            restResponse.setStatus(HttpStatus.ACCEPTED.toString());
            return restResponse;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return RestResponse.builder().build();
    }

    @Override
    public UserDto findByUserId(Long userId) {
        log.info("UserServiceImpl findByUserId {}", userId);
        try {
            return userDao.findById(userId).stream().map(UserMapper::toDto)
                    .findFirst().orElse(null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
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
