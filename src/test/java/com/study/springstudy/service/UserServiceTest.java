package com.study.springstudy.service;

import com.study.springstudy.domain.Order;
import com.study.springstudy.domain.User;
import com.study.springstudy.dto.UserCreateRequest;
import com.study.springstudy.dto.UserResponse;
import com.study.springstudy.exception.OrderAccessDeniedException;
import com.study.springstudy.exception.UserNotFoundException;
import com.study.springstudy.repository.OrderRepository;
import com.study.springstudy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserTest() {

        //given
        User user = new User(
                1L,
                "Kim",
                25
                );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        //when
        UserResponse response = userService.getUser(1L);

        //then
        assertEquals(1L,response.getId());
        assertEquals("Kim",response.getName());
        assertEquals(25,response.getAge());

        //verify
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserNotFoundTest() {

        //given
        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        //when & then
        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUser(999L)
        );
    }

    @Test
    void createUserTest() {

        //given
        UserCreateRequest request =
                new UserCreateRequest("Kim",25);

        User savedUser =
                new User(1L,"Kim",25);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        //when
        UserResponse response = userService.createUser(request);

        //then
        assertEquals(1L,response.getId());
        assertEquals("Kim",response.getName());
        assertEquals(25,response.getAge());

        //verify
        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User capturedUser = captor.getValue();

        assertEquals("Kim", capturedUser.getName());
        assertEquals(25, capturedUser.getAge());
    }

    @Test
    void removeUserOrder() {

        //given
        User requestUser = new User(
                3L,
                "Kim",
                25
        );

        User orderOwner = new User(
                5L,
                "Lee",
                30
        );

        Order order = new Order(
                2L,
                "keybord",
                50000,
                orderOwner
        );

        when(userRepository.findById(3L))
                .thenReturn(Optional.of(requestUser));

        when(orderRepository.findById(2L))
                .thenReturn(Optional.of(order));

        //when & then
        assertThrows(
                OrderAccessDeniedException.class,
                () -> userService.removeUserOrder(3L,2L)
        );

        //verify
        verify(userRepository).findById(3L);
        verify(orderRepository).findById(2L);
    }
}
