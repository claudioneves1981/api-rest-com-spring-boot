package dev.claudioneves.apirestdozero.controller;


import dev.claudioneves.apirestdozero.controller.dto.AuthenticateUserRequestDTO;
import dev.claudioneves.apirestdozero.controller.dto.AuthenticateUserResponseDTO;
import dev.claudioneves.apirestdozero.service.AuthenticateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticateService authenticateService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticateUserResponseDTO> authenticate(@RequestBody @Valid AuthenticateUserRequestDTO authenticateUserRequestDTO){


        String accessToken = authenticateService.authenticate(
                authenticateUserRequestDTO.username(),
                authenticateUserRequestDTO.password()
        );

        var response = new AuthenticateUserResponseDTO(accessToken);

        return ResponseEntity.ok(response);

    }

}
