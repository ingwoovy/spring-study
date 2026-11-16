package com.study.springstudy.service;

import com.study.springstudy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.study.springstudy.domain.User;
import com.study.springstudy.dto.RegisterRequest;import org.mockito.ArgumentCaptor;

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

        verify(userRepository).save(captor.capture());

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

        assertThrows(IllegalArgumentException.class,
                () -> authService.register(request));

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}