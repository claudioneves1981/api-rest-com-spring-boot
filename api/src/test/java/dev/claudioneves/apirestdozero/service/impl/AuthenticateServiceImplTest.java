package dev.claudioneves.apirestdozero.service.impl;

import dev.claudioneves.apirestdozero.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticateServiceImplTest {


    private AuthenticateServiceImpl authenticateServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock private JwtServiceImpl jwtService;

    @Mock private Authentication authentication;


    @BeforeAll
    void setup(){
        MockitoAnnotations.openMocks(this);
        this.authenticateServiceImpl = new AuthenticateServiceImpl(this.authenticationManager, this.jwtService);
    }

    @BeforeEach
    void reserMocks(){
        Mockito.reset(this.authenticationManager);
        Mockito.reset(this.jwtService);
    }



    @Test
    void itShouldAuthenticateUserAndGenerateToken(){

        var username = "john.doe@gmail.com";
        var password = "123456";
        when(this.authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(this.jwtService.generateToken(any()))
                .thenReturn("accessToken");

        var result = this.authenticateServiceImpl.authenticate(username,password);

        assertEquals("accessToken", result);
        verify(this.authenticationManager, times(1))
                .authenticate(any());

        verify(this.jwtService, times(1))
                .generateToken(any());

    }


}
