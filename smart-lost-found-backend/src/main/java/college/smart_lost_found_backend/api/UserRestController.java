package college.smart_lost_found_backend.api;

import college.smart_lost_found_backend.dto.UserDto;
import college.smart_lost_found_backend.service.JwtService;
import college.smart_lost_found_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserRestController {

    private final UserService userService;

    private final JwtService  jwtService;


    private final AuthenticationManager authenticationManager;


    public  UserRestController(UserService userService,
                               JwtService jwtService,
                               AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/users")
    public ResponseEntity<String> save(@RequestBody UserDto userDto) {
      log.info("UserRestController save userDto ");
      return new ResponseEntity<>(userService.save(userDto), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody UserDto authRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(),
                authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authentication.getName());
        } else {
            throw new RuntimeException("invalid user request !");
        }


    }


}
