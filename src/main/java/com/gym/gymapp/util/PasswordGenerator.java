package com.gym.gymapp.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * პაროლის გენერატორი.
 * ქმნის მითითებული სიგრძის შემთხვევით სტრინგს,
 * რომელიც შეიცავს A-Z, a-z, 0-9 სიმბოლოებს.
 */
@Component
public class PasswordGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generate(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }
}
