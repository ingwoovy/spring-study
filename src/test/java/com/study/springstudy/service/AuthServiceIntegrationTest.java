package com.study.springstudy.service;

import com.study.springstudy.domain.User;
import com.study.springstudy.dto.RegisterRequest;
import com.study.springstudy.exception.DuplicateEmailException;
import com.study.springstudy.global.jwt.JwtUtil;
import com.study.springstudy.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("flywaytest")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Import(AuthService.class)
class AuthServiceIntegrationTest {

    @MockitoSpyBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Test
    void registerDuplicateEmailFromRealDatabase() {

        // given
        String email = UUID.randomUUID() + "@example.com";

        User existingUser = new User(
                "existingUser",
                25,
                email,
                "encodedPassword"
        );

        userRepository.saveAndFlush(existingUser);

        RegisterRequest request = new RegisterRequest(
                "newUser",
                25,
                email,
                "hello1234"
        );

        doReturn(false)
                .when(userRepository)
                .existsByEmail(email);

        when(passwordEncoder.encode("hello1234"))
                .thenReturn("encodedPassword");

        // when & then
        assertThrows(
                DuplicateEmailException.class,
                () -> authService.register(request)
        );

        verify(userRepository).existsByEmail(email);
        verify(userRepository, times(2))
                .saveAndFlush(any(User.class));
    }
}