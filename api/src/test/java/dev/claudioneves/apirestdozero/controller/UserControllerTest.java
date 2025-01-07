package dev.claudioneves.apirestdozero.controller;

import dev.claudioneves.apirestdozero.controller.dto.CreateUserRequestDTO;
import dev.claudioneves.apirestdozero.controller.dto.UpdateUserRequestDTO;
import dev.claudioneves.apirestdozero.service.UserService;
import dev.claudioneves.apirestdozero.service.dto.CreateUserCommand;
import dev.claudioneves.apirestdozero.service.dto.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeAll
    void setUp(){
        MockitoAnnotations.openMocks(this);
        this.userController = new UserController(this.userService);
    }

    @BeforeEach
    void setUpEach(){
        reset(this.userService);
    }

    @Test
    void itShouldCallServiceAndReturnCreatedUserID(){

        var request = new CreateUserRequestDTO(
                "John Doe",
                "john.doe@gmail.com",
                "123456"
        );

        when(this.userService.createUser(any()))
                .thenReturn(java.util.UUID.randomUUID());

        var response = this.userController.createUser(request);
        var body = response.getBody();

        assertNotNull(body.id());

        var captor = ArgumentCaptor.forClass(CreateUserCommand.class);

        verify(this.userService, times(1)).createUser(captor.capture());

        var command = captor.getValue();
        assertEquals(command.email(), request.email());
        assertEquals(command.name(), request.name());
        assertEquals(command.password(), request.password());

    }

    @Test
    void itShouldReturnUserByID(){

        var uuid = UUID.randomUUID();
        var userDTO = new UserDTO(

                uuid,
                "John Doe",
                "john.doe@gmail.com",
                LocalDateTime.now(),
                LocalDateTime.now()

        );

        when(this.userService.getUserByID(uuid))
                .thenReturn(userDTO);

        var response = this.userController.getUserByID(uuid);
        var body = response.getBody();

        assertEquals(body.id(), userDTO.id());
        assertEquals(body.name(),userDTO.name());
        assertEquals(body.email(),userDTO.email());
        assertEquals(body.createdAt(),userDTO.createdAt().toString());
        assertEquals(body.updatedAt(),userDTO.updatedAt().toString());

        verify(this.userService, times(1))
                .getUserByID(uuid);

    }

    @Test
    void itShouldDeleteUserByID(){

        var uuid = UUID.randomUUID();
        this.userController.deleteUserByID(uuid);

        verify(this.userService, times(1))
                .deleteUserByID(uuid);

    }

    @Test
    void itShouldUpdateUserByID(){
        var uuid = UUID.randomUUID();
        var request = new UpdateUserRequestDTO("123456");
        this.userController.updateUser(uuid,request);
        verify(this.userService, times(1))
                .updateUser(uuid, request.password());
    }

    @Test
    void itShouldReturnAuthenticatedUser(){


        var userDTO = new UserDTO(

                UUID.randomUUID(),
                "John Doe",
                "john.doe@gmail.com",
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        var authToken = new UsernamePasswordAuthenticationToken(
                userDTO,
                null,
                Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        var response = this.userController.geAuthenticateUser();
        var body = response.getBody();

        assertEquals(body.id(),userDTO.id());
        assertEquals(body.name(), userDTO.name());
        assertEquals(body.email(), userDTO.email());



    }


}
