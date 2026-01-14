package com.example.errorstory.service;

import com.example.errorstory.model.User;
import com.example.errorstory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // CREATE (Register)
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // READ (Login)
    public User login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    // READ ALL USERS (optional admin feature)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // UPDATE
    public User updateUser(Long id, User updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existing);
    }

    // DELETE
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
