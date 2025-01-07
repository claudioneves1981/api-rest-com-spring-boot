package dev.claudioneves.apirestdozero.service.impl;

import dev.claudioneves.apirestdozero.repository.UserRepository;
import dev.claudioneves.apirestdozero.service.JwtService;
import dev.claudioneves.apirestdozero.service.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final UserRepository userRepository;

    @Value("${security.jwt.secret-key:W1EzKKHt9bOEQEROqvnBFPV0rplU8X45}")
    private String secret;

    @Value("${security.jwt.expiration:360000}")
    private Long expiration;

    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(this.getSecretKey())
                .compact();


    }


    public boolean isTokenValid(String token) {

        try{
            return this.getClaims(token)
                    .getExpiration()
                    .after(new Date());

        }catch(ExpiredJwtException e){
            return false;
        }

    }


    public UserDTO getUserDetails(String token) {

        return this.userRepository
                .findByEmail(getClaims(token).getSubject())
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                ))
                .orElseThrow();

    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(this.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }
}
