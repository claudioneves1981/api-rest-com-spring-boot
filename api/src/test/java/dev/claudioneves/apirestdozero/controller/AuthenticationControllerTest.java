package dev.claudioneves.apirestdozero.controller;


import dev.claudioneves.apirestdozero.controller.dto.AuthenticateUserRequestDTO;
import dev.claudioneves.apirestdozero.service.AuthenticateService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerTest {

    private AuthenticationController authenticationController;

    @Mock
    private AuthenticateService service;

    @BeforeAll
    void setup(){
        MockitoAnnotations.openMocks(this);
        this.authenticationController = new AuthenticationController(this.service);
    }

    @BeforeEach
    void resetMocks(){
        reset(this.service);
    }



    @Test
    void itShouldReturnAccessToken(){

        var request = new AuthenticateUserRequestDTO(
                "john.doe@gmail.com",
                "123456"
        );

        when(this.service.authenticate(request.username(),request.password()))
                .thenReturn("accessToken");

        var response = this.authenticationController.authenticate(request);
        var body = response.getBody();

        assertEquals(body.accessToken(),"accessToken");

        verify(this.service, times(1)).authenticate(request.username(),request.password());

    }

}
