package com.gym.gymapp.service;

import com.gym.gymapp.exception.NotFoundException;
import com.gym.gymapp.exception.ValidationException;
import com.gym.gymapp.model.User;
import com.gym.gymapp.repository.UserRepository;
import com.gym.gymapp.util.PasswordGenerator;
import com.gym.gymapp.util.PasswordHasher;
import com.gym.gymapp.util.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public record CreatedUser(User user, String rawPassword) {}

    private final UserRepository userRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGenerator passwordGenerator;
    private final PasswordHasher passwordHasher;

    public UserService(UserRepository userRepository,
                       UsernameGenerator usernameGenerator,
                       PasswordGenerator passwordGenerator,
                       PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.usernameGenerator = usernameGenerator;
        this.passwordGenerator = passwordGenerator;
        this.passwordHasher = passwordHasher;
    }

    public CreatedUser createUser(String firstName, String lastName, boolean active) {
        if (firstName==null || firstName.isBlank() || lastName==null || lastName.isBlank())
            throw new ValidationException("firstName/lastName required");

        String username = usernameGenerator.generate(firstName, lastName);
        String raw = passwordGenerator.generate(10);

        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setUsername(username);
        u.setPasswordHash(passwordHasher.encode(raw));
        u.setIsActive(active);

        User saved = userRepository.save(u);
        log.info("Created user id={}, username={}", saved.getId(), saved.getUsername());
        return new CreatedUser(saved, raw);
    }

    public void changePassword(String username, String oldRaw, String newRaw) {
        User u = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));

        if (!passwordHasher.matches(oldRaw, u.getPasswordHash()))
            throw new ValidationException("Old password doesn't match");
        if (newRaw == null || newRaw.length() < 6)
            throw new ValidationException("New password must be at least 6 chars");

        u.setPasswordHash(passwordHasher.encode(newRaw));
        userRepository.save(u);
        log.info("Password changed for username={}", username);
    }

    public void activate(String username) {
        User u = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
        if (Boolean.TRUE.equals(u.getIsActive()))
            throw new ValidationException("User already active");
        u.setIsActive(true);
        userRepository.save(u);
        log.info("Activated user username={}", username);
    }

    public void deactivate(String username) {
        User u = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
        if (Boolean.FALSE.equals(u.getIsActive()))
            throw new ValidationException("User already inactive");
        u.setIsActive(false);
        userRepository.save(u);
        log.info("Deactivated user username={}", username);
    }
}
