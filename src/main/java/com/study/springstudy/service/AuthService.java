package com.study.springstudy.service;

import com.study.springstudy.domain.User;
import com.study.springstudy.dto.RegisterRequest;
import com.study.springstudy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterRequest request) {

        boolean exists = userRepository.existsByEmail(request.getEmail());

        if (exists) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName(),
                request.getAge(),
                request.getEmail(),
                encodedPassword
        );

        userRepository.save(user);
    }
}