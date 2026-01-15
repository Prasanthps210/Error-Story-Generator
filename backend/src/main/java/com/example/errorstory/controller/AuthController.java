package com.example.errorstory.controller;

import com.example.errorstory.model.User;
import com.example.errorstory.security.JwtUtil;
import com.example.errorstory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class  AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // REGISTER
//    @PostMapping("/register")
//    public User register(@RequestBody User user) {
//        return userService.register(user);
//    }
//
//    // LOGIN
//    @PostMapping("/login")
//    public User login(@RequestBody Map<String, String> data) {
//        String username = data.get("username");
//        String password = data.get("password");
//        return userService.login(username, password);
//    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User loggedIn = userService.login(user.getUsername(), user.getPassword());
            String token = jwtUtil.generateToken(loggedIn.getUsername());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "user", loggedIn
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
