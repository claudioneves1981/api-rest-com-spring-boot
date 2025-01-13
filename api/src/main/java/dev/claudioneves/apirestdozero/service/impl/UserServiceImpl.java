package dev.claudioneves.apirestdozero.service.impl;

import dev.claudioneves.apirestdozero.domain.User;
import dev.claudioneves.apirestdozero.repository.UserRepository;
import dev.claudioneves.apirestdozero.service.UserService;
import dev.claudioneves.apirestdozero.service.dto.CreateUserCommand;
import dev.claudioneves.apirestdozero.service.dto.UserDTO;
import dev.claudioneves.apirestdozero.service.exceptions.EmailAlreadyExistsException;
import dev.claudioneves.apirestdozero.service.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UUID createUser(CreateUserCommand command) {

        var userOptional = userRepository.findByEmail(command.email());

        if(userOptional.isPresent()){

            throw new EmailAlreadyExistsException();

        }

        var passwordEncoded = encoder.encode(command.password());

        var user = User.builder()
                .name(command.name())
                .email(command.email())
                .password(passwordEncoded)
                .build();

        this.userRepository.createUser(user);
        return user.getId();
    }

    public UserDTO getUserByID(UUID id) {
        var user = this.userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return new UserDTO(

                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()


        );
    }


    public void deleteUserByID(UUID id) {
        this.userRepository.deleteUserById(id);

    }

    public void updateUser(UUID id, String password){
        var user = this.userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        var passwordEncoded = encoder.encode(password);
        user.setPassword(passwordEncoded);
        this.userRepository.save(user);
    }
}
