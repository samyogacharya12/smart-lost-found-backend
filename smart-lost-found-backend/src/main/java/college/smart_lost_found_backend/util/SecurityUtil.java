package college.smart_lost_found_backend.util;

import college.smart_lost_found_backend.dao.UserDao;
import college.smart_lost_found_backend.dto.UserDto;
import college.smart_lost_found_backend.model.User;
import college.smart_lost_found_backend.service.UserService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
@Slf4j
public class SecurityUtil {

    @Autowired
    private UserService userService;

    public static String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication.getName();
    }

    public static UserDto getCurrentUser() {
        try {
            String username = getCurrentUsername();
            Optional<UserDto> user = userService.findByUsername(username);
            return user.orElse(null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
