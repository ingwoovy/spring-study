package com.study.springstudy.service;

import com.study.springstudy.domain.User;
import com.study.springstudy.dto.RegisterRequest;
import com.study.springstudy.exception.InvalidCredentialsException;
import com.study.springstudy.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.study.springstudy.dto.LoginRequest;
import com.study.springstudy.global.jwt.JwtUtil;
import com.study.springstudy.exception.DuplicateEmailException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void register(RegisterRequest request) {

        boolean exists = userRepository.existsByEmail(request.getEmail());

        if (exists) {
            throw new DuplicateEmailException();
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName(),
                request.getAge(),
                request.getEmail(),
                encodedPassword
        );

        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            if (isDuplicateEmailViolation(e)) {
                throw new DuplicateEmailException();
            }
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));

        boolean matches = passwordEncoder.matches(
                request.getPassword(), user.getPassword()
        );

        if (!matches) {
            throw new InvalidCredentialsException( "이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        return jwtUtil.generateToken(user.getEmail());
    }

    private boolean isDuplicateEmailViolation(Throwable e) {

        Throwable cause = e;

        while (cause != null) {

            if (cause instanceof ConstraintViolationException exception) {
                if ("23505".equals(exception.getSQLState())
                        && "uk_users_email".equals(exception.getConstraintName())) {
                    return true;
                }
            }
            if (cause instanceof PSQLException exception) {

                ServerErrorMessage error = exception.getServerErrorMessage();

                if ("23505".equals(exception.getSQLState())
                        && error != null
                        && "uk_users_email".equals(error.getConstraint())) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }
}