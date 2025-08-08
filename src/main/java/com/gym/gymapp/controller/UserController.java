package com.gym.gymapp.controller;

import com.gym.gymapp.model.User;
import com.gym.gymapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ყველა იუზერის წამოღება
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // იუზერის წამოღება username-ის მიხედვით
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ახალი იუზერის რეგისტრაცია
    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // თუ username უკვე არსებობს -> error
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        // პაროლის დაშიფვრა
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    // ავტორიზაცია (Login)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        return userRepository.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                        return ResponseEntity.ok("Login successful!");
                    } else {
                        return ResponseEntity.status(401).body("Invalid password");
                    }
                })
                .orElse(ResponseEntity.status(404).body("User not found"));
    }
}
