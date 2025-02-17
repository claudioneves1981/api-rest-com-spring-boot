package dev.claudioneves.apirestdozero.service.impl;


import dev.claudioneves.apirestdozero.service.AuthenticateService;
import dev.claudioneves.apirestdozero.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateServiceImpl implements AuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String authenticate(String username, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        var authentication = this.authenticationManager.authenticate(authenticationToken);
        return this.jwtService.generateToken((UserDetails) authentication.getPrincipal());

    }
}
