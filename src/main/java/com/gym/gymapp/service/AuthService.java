package com.gym.gymapp.service;

import com.gym.gymapp.exception.AuthException;
import com.gym.gymapp.exception.NotFoundException;
import com.gym.gymapp.model.User;
import com.gym.gymapp.repository.UserRepository;
import com.gym.gymapp.util.PasswordHasher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordHasher hasher;

    public AuthService(UserRepository userRepository, PasswordHasher hasher) {
        this.userRepository = userRepository;
        this.hasher = hasher;
    }

    public User requireAuth(String username, String rawPassword) {
        User u = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
        if (!hasher.matches(rawPassword, u.getPasswordHash()))
            throw new AuthException("Invalid credentials");
        return u;
    }
}
