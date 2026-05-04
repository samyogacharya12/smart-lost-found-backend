package college.smart_lost_found_backend.api;

import college.smart_lost_found_backend.dto.RestResponse;
import college.smart_lost_found_backend.dto.UserDto;
import college.smart_lost_found_backend.service.AuthenticationService;
import college.smart_lost_found_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserRestController {

    private final UserService userService;

    private final AuthenticationService authenticationService;


    public UserRestController(UserService userService,
                              AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/users")
    public ResponseEntity<RestResponse> save(@RequestBody UserDto userDto) {
        log.info("UserRestController save userDto ");
        return new ResponseEntity<>(userService.save(userDto), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserDto> authenticateAndGetToken(@RequestBody UserDto authRequest) {
        UserDto userDto = authenticationService.login(authRequest);
        log.info("UserRestController authenticate userDto ");
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<RestResponse> verifyEmail(@RequestParam String token) {
       RestResponse restResponse= userService.verifyEmail(token);
        return ResponseEntity.ok(restResponse);
    }


}
