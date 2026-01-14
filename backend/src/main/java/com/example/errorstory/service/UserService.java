package com.example.errorstory.service;

import com.example.errorstory.model.User;
import com.example.errorstory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // REGISTER
    public User register(User user) {

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // LOGIN
    public User login(String username, String rawPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    // UPDATE OWN PROFILE (optional but useful)
    public User updateProfile(Long id, User updatedUser) {

        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUser.getUsername() != null &&
                !updatedUser.getUsername().equals(existing.getUsername())) {

            if (userRepository.findByUsername(updatedUser.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }
            existing.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getEmail() != null &&
                !updatedUser.getEmail().equals(existing.getEmail())) {

            if (userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }
            existing.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existing);
    }

    // DELETE OWN ACCOUNT (optional)
    public void deleteAccount(Long id) {
        userRepository.deleteById(id);
    }
}
