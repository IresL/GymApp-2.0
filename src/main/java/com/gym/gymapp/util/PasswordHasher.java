package com.gym.gymapp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {
    private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder(10);
    public String encode(String raw){ if (raw==null||raw.isBlank()) throw new IllegalArgumentException("raw required"); return enc.encode(raw); }
    public boolean matches(String raw, String hash){ return raw!=null && hash!=null && enc.matches(raw, hash); }
}
