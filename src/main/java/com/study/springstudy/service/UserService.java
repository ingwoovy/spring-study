package com.study.springstudy.service;

import com.study.springstudy.domain.User;
import com.study.springstudy.dto.UserCreateRequest;
import com.study.springstudy.dto.UserResponse;
import com.study.springstudy.dto.UserUpdateRequest;
import com.study.springstudy.exception.UserNotFoundException;
import com.study.springstudy.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getAge()
        );
    }

    public List<UserResponse> getAllUsers () {
        Sort sort = Sort.by(
                    Sort.Direction.ASC,
                "age"
        );

        List<User> users = userRepository.findAll(sort);

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getAge()
                ))
                .toList();
    }

    public Page<UserResponse> getUsersByPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.ASC,
                        "age"
                ));
        Page<User> users = userRepository.findAll(pageable);

        return users.map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getAge()
                ));
    }

    public List<UserResponse> getUserByName(String name) {
        List<User> users = userRepository.findByName(name);

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getAge()
                ))
                .toList();
    }

    public List<UserResponse> getUsersByMinAge(Integer age) {
        List<User> users = userRepository.findByAgeGreaterThanEqual(age);

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getAge()
                ))
                .toList();
    }

    public List<UserResponse> getUserByNameAndMinAge(
            String name, Integer minAge
    ) {
        List<User> users = userRepository.findByNameAndAgeGreaterThanEqual(name, minAge);

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getAge()
                ))
                .toList();
    }

    public UserResponse createUser(UserCreateRequest request) {
        User user = new User(
                null,
                request.getName(),
                request.getAge()
        );
        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getAge()
        );
    }

    @Transactional
    public UserResponse updateUser(
            Long id,
            UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.update(
                request.getName(),
                request.getAge()
        );

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getAge()
        );
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(id);

    }
}
