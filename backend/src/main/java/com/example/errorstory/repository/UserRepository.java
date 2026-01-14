package com.example.errorstory.repository;

import com.example.errorstory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (used for login)
    Optional<User> findByUsername(String username);

    // Find user by email (optional future use)
    Optional<User> findByEmail(String email);

    // CRUD already included:
    // save(User u)
    // findById(Long id)
    // findAll()
    // delete(User u)
    // deleteById(Long id)
}
