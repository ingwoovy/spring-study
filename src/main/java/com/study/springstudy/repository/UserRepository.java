package com.study.springstudy.repository;

import com.study.springstudy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);
    List<User> findByAgeGreaterThanEqual(Integer age);
    List<User> findByNameAndAgeGreaterThanEqual(String name, Integer age);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
