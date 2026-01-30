package com.garage.system.controller;

import com.garage.system.model.User;
import com.garage.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null && user.getPasswordHash().equals(password)) {
            // In a real app, generate a token or set a session.
            // For now, we just return 200 OK.
            return ResponseEntity.ok().body(
                    Map.of("message", "Login successful", "role", user.getRole(), "username", user.getUsername()));
        }

        return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }

    // Endpoint to create an admin user initially (for testing)
    @PostMapping("/setup-admin")
    public ResponseEntity<?> setupAdmin() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", "admin123", "ADMIN");
            userRepository.save(admin);
            return ResponseEntity.ok("Admin created");
        }
        return ResponseEntity.ok("Admin already exists");
    }
}
