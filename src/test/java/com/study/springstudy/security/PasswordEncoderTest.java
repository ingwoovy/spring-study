package com.study.springstudy.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Test
    void passwordEncodeTest() {

        // Given
        String rawPassword = "hello1234";

        // When
        String encodedPassword =
                passwordEncoder.encode(rawPassword);

        // Then
        assertNotEquals(rawPassword,encodedPassword);

        assertTrue(
                passwordEncoder.matches(rawPassword,encodedPassword)
        );
        assertFalse(
                passwordEncoder.matches("wrongPassword",encodedPassword)
        );
    }
}