package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.UserDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);
    Date extractExpiration(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(String userName);

    Boolean  validateToken(final String token,  UserDetails userDetails);

    Key getSignKey();
}
