package com.labhesh.Todos.Todos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    @Value("${password.salt}")
    private static String salt;

    public static String encodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String saltedPassword = password + salt;
        return passwordEncoder.encode(saltedPassword);
    }

    public static boolean matches(String enteredPassword, String storedHashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String saltedEnteredPassword = enteredPassword + salt;
        return !passwordEncoder.matches(saltedEnteredPassword, storedHashedPassword);
    }
}

