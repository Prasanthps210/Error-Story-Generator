package com.example.errorstory.controller;

import com.example.errorstory.model.User;
import com.example.errorstory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    // REGISTER
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    // LOGIN
    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        return userService.login(username, password);
    }

    // UPDATE USER
    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // DELETE USER
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
}
