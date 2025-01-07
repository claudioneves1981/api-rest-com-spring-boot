package dev.claudioneves.apirestdozero.repository;

import dev.claudioneves.apirestdozero.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

   Optional<User> findByEmail(String email);
   Optional<User> findById(UUID id);
   User createUser(User user);
   void deleteUserByID(UUID id);
   User save(User user);
}
