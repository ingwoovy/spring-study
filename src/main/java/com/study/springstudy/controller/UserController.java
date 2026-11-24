package com.study.springstudy.controller;

import com.study.springstudy.dto.OrderResponse;
import com.study.springstudy.dto.UserCreateRequest;
import com.study.springstudy.dto.UserResponse;
import com.study.springstudy.dto.UserUpdateRequest;
import com.study.springstudy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import com.study.springstudy.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/users", params = {"page", "size"})
    public Page<UserResponse> getUsersByPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return userService.getUsersByPage(page, size);
    }

    @GetMapping(value = "/users", params = "name")
    public List<UserResponse> getUserByName(
            @RequestParam String name
    ) {
        return userService.getUserByName(name);
    }

    @GetMapping(value = "/users", params = "minAge")
    public List<UserResponse> getUserByMinAge(
            @RequestParam Integer minAge
    ) {
        return userService.getUsersByMinAge(minAge);
    }

    @GetMapping(value = "/users", params = {"name", "minAge"})
    public List<UserResponse> getUserByNameAndMinAge (
            @RequestParam String name,
            @RequestParam Integer minAge
    ) {
        return userService.getUserByNameAndMinAge(name, minAge);
    }

    @GetMapping("/users/{id}/orders")
    public List<OrderResponse> getUserOrders (@PathVariable Long id,
                                              @AuthenticationPrincipal User loginUser) {
        return userService.getUserOrders(id, loginUser.getId());
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody
            @Valid UserCreateRequest request
    ) {
        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PatchMapping("/users/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal User loginUser
    ) {
        UserResponse response = userService.updateUser(id, request,loginUser.getId());

        return response;
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
                                           @AuthenticationPrincipal User loginUser) {
        userService.deleteUser(id, loginUser.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{userId}/orders/{orderId}")
    public ResponseEntity<Void> removeUserOrder(
            @PathVariable Long userId,
            @PathVariable Long orderId,
            @AuthenticationPrincipal User loginUser
    ) {
        userService.removeUserOrder(userId,orderId,loginUser.getId());
        return ResponseEntity.noContent().build();
    }
}
