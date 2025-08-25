package com.gym.gymapp.util;

import com.gym.gymapp.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
    private final UserRepository userRepository;
    public UsernameGenerator(UserRepository userRepository){ this.userRepository=userRepository; }

    public String generate(String firstName, String lastName) {
        if (firstName==null || lastName==null) throw new IllegalArgumentException("first/last required");
        String base = (firstName.trim() + "." + lastName.trim()).replaceAll("\\s+","").toLowerCase();
        String candidate = base;
        int i = 1;
        while (userRepository.existsByUsernameIgnoreCase(candidate)) {
            candidate = base + "." + i++;
        }
        return candidate;
    }
}
