package dev.claudioneves.apirestdozero.repository;

import dev.claudioneves.apirestdozero.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

//@Repository
public interface UserRepository {//extends JpaRepository<User, UUID> {

   Optional<User> findByEmail(String email);
   Optional<User> findById(UUID id);
   User createUser(User user);
   void deleteUserById(UUID id);
   User save(User user);
}
