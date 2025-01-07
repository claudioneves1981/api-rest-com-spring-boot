package dev.claudioneves.apirestdozero.controller;

import dev.claudioneves.apirestdozero.controller.dto.CreateUserRequestDTO;
import dev.claudioneves.apirestdozero.controller.dto.CreateUserResponseDTO;
import dev.claudioneves.apirestdozero.controller.dto.GetUserByIDResponseDTO;
import dev.claudioneves.apirestdozero.controller.dto.UpdateUserRequestDTO;
import dev.claudioneves.apirestdozero.service.UserService;
import dev.claudioneves.apirestdozero.service.dto.CreateUserCommand;
import dev.claudioneves.apirestdozero.service.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@Validated
@Tag(name = "User")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<CreateUserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {

        var command = new CreateUserCommand(
                createUserRequestDTO.name(),
                createUserRequestDTO.email(),
                createUserRequestDTO.password()

        );

        var userID = this.userService.createUser(command);
        var response = new CreateUserResponseDTO(userID);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserByIDResponseDTO> getUserByID(@PathVariable UUID id) {

        var userDTO = this.userService.getUserByID(id);
        var response = new GetUserByIDResponseDTO(

                userDTO.id(),
                userDTO.name(),
                userDTO.email(),
                userDTO.createdAt().toString(),
                userDTO.updatedAt().toString()

        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserByIDResponseDTO> geAuthenticateUser() {

        var userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = new GetUserByIDResponseDTO(

                userDTO.id(),
                userDTO.name(),
                userDTO.email(),
                userDTO.createdAt().toString(),
                userDTO.updatedAt().toString()

        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserByID(@PathVariable UUID id) {
        this.userService.deleteUserByID(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {

        this.userService.updateUser(id, updateUserRequestDTO.password());
        return ResponseEntity.noContent().build();

    }


}
