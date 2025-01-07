package dev.claudioneves.apirestdozero.service;

import dev.claudioneves.apirestdozero.service.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token);
    UserDTO getUserDetails(String token);

}
