package com.study.springstudy.service;

import com.study.springstudy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.study.springstudy.domain.User;
import com.study.springstudy.dto.RegisterRequest;
import org.mockito.ArgumentCaptor;
import com.study.springstudy.dto.LoginRequest;
import java.util.Optional;
import com.study.springstudy.exception.InvalidCredentialsException;
import com.study.springstudy.global.jwt.JwtUtil;
import com.study.springstudy.exception.DuplicateEmailException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerSuccess() {
        // given
        RegisterRequest request = new RegisterRequest(
                "홍길동",
                25,
                "hong@example.com",
                "hello1234"
        );
        when(userRepository.existsByEmail("hong@example.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("hello1234"))
                .thenReturn("encodedPassword");

        // when
        authService.register(request);

        // then
        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).saveAndFlush(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("hong@example.com",savedUser.getEmail());
        assertEquals("encodedPassword",savedUser.getPassword());
    }

    @Test
    void registerDuplicateEmail() {

        // given
        RegisterRequest request = new RegisterRequest(
                "홍길동",
                25,
                "hong@example.com",
                "hello1234"
        );
        when(userRepository.existsByEmail("hong@example.com"))
                .thenReturn(true);

        assertThrows(DuplicateEmailException.class,
                () -> authService.register(request));

        verify(userRepository, never()).saveAndFlush(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void loginSuccess() {

        // given
        LoginRequest request = new LoginRequest(
                "hong@example.com",
                "hello1234"
        );

        User user = new User(
                "홍길동",
                25,
                "hong@example.com",
                "encodedPassword"
        );
        when(userRepository.findByEmail("hong@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("hello1234","encodedPassword"))
                .thenReturn(true);

        when(jwtUtil.generateToken("hong@example.com"))
                .thenReturn("test.jwt.token");

        // when
        String token = authService.login(request);

        // then
        assertEquals("test.jwt.token", token);
        verify(jwtUtil).generateToken("hong@example.com");
    }

    @Test
    void loginWrongPassword() {

        // given
        LoginRequest request = new LoginRequest(
                "hong@example.com",
                "wrong1234"
        );
        User user = new User(
                "홍길동",
                25,
                "hong@example.com",
                "encodedPassword"
        );
        when(userRepository.findByEmail("hong@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong1234","encodedPassword"))
                .thenReturn(false);

        // when & then
        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void loginUserNotFound() {

        // given
        LoginRequest request = new LoginRequest(
                "unknown@example.com",
                "hello1234"
        );

        when(userRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );
        verify(passwordEncoder, never()).matches(
                anyString(),
                anyString()
        );
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void registerDuplicateEmailFromDatabase() {

        // given
        RegisterRequest request = new RegisterRequest(
                "홍길동",
                25,
                "hong@example.com",
                "hello1234"
        );

        when(userRepository.existsByEmail("hong@example.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("hello1234"))
                .thenReturn("encodedPassword");

        SQLException sqlException =
                new SQLException("Unique violation", "23505");

        ConstraintViolationException constraintException =
                new ConstraintViolationException(
                        "Constraint violation",
                        sqlException,
                        "uk_users_email"
                );

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException(
                        "Data integrity violation",
                        constraintException
                );

        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(exception);

        // when & then
        assertThrows(
                DuplicateEmailException.class,
                () -> authService.register(request)
        );

        verify(userRepository).saveAndFlush(any(User.class));
    }

    @Test
    void registerOtherConstraintViolation() {

        // given
        RegisterRequest request = new RegisterRequest(
                "홍길동",
                25,
                "hong@example.com",
                "hello1234"
        );

        when(userRepository.existsByEmail("hong@example.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("hello1234"))
                .thenReturn("encodedPassword");

        SQLException sqlException =
                new SQLException("Unique violation", "23505");

        ConstraintViolationException constraintException =
                new ConstraintViolationException(
                        "Constraint violation",
                        sqlException,
                        "users_pkey"
                );

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException(
                        "Data integrity violation",
                        constraintException
                );

        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(exception);

        // when & then
        DataIntegrityViolationException result = assertThrows(
                DataIntegrityViolationException.class,
                () -> authService.register(request)
        );

        assertSame(exception, result);

        verify(userRepository).saveAndFlush(any(User.class));
    }
}