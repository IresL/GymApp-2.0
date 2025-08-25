package com.gym.gymapp.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RNG = new SecureRandom();

    public String generate(){ return generate(10); }
    public String generate(int len){
        if (len<=0) throw new IllegalArgumentException("length>0");
        StringBuilder sb = new StringBuilder(len);
        for (int i=0;i<len;i++) sb.append(ALPHABET.charAt(RNG.nextInt(ALPHABET.length())));
        return sb.toString();
    }
}
