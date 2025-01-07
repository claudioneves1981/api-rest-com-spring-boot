package dev.claudioneves.apirestdozero.service;

import dev.claudioneves.apirestdozero.domain.User;
import dev.claudioneves.apirestdozero.service.dto.CreateUserCommand;
import dev.claudioneves.apirestdozero.service.dto.UserDTO;

import java.util.UUID;

public interface UserService {

    UUID createUser(CreateUserCommand command);
    UserDTO getUserByID(UUID id);
    void deleteUserByID(UUID id);
    void updateUser(UUID id, String password);
}
