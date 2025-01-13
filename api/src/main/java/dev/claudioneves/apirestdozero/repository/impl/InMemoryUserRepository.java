package dev.claudioneves.apirestdozero.repository.impl;

import dev.claudioneves.apirestdozero.domain.User;
import dev.claudioneves.apirestdozero.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Repository
public class InMemoryUserRepository{ //implements UserRepository {

    private final List<User> users = new ArrayList<>();

    public Optional<User> findByEmail(String email) {
        return  users.stream()
                .filter(user -> email.equals(user.getEmail()))
                .findAny();
    }

    public User createUser(User user) {

        this.users.add(user);
        return user;
    }

    public Optional<User> findById(UUID id) {
        return this.users.stream()
                .filter(user -> id.equals(user.getId()))
                .findAny();
    }

    public void deleteUserByID(UUID id) {
            this.users.removeIf(user -> id.equals(user.getId()));
    }


    public User save(User user) {
       this.users.removeIf(u -> user.getId().equals(u.getId()));
       this.users.add(user);
       return user;
    }


}
