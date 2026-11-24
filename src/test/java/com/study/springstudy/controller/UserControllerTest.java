package com.study.springstudy.controller;

import com.study.springstudy.dto.UserCreateRequest;
import com.study.springstudy.dto.UserResponse;
import com.study.springstudy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.study.springstudy.exception.UserNotFoundException;
import org.springframework.http.MediaType;
import tools.jackson.databind.ObjectMapper;
import com.study.springstudy.exception.OrderAccessDeniedException;
import com.study.springstudy.domain.User;
import com.study.springstudy.exception.UserAccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void getUserTest() throws Exception {

        // given
        UserResponse response = new UserResponse(1L, "Kim", 25);

        when(userService.getUser(1L))
                .thenReturn(response);

        // when & then
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Kim"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void getUserNotFoundTest() throws Exception {

        // given
        when(userService.getUser(999L))
                .thenThrow(new UserNotFoundException());

        // when & then
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUserTest() throws Exception {

        // given
        UserCreateRequest request = new UserCreateRequest("Kim", 25);

        UserResponse response = new UserResponse(1L, "Kim", 25);

        when(userService.createUser(any(UserCreateRequest.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Kim"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void createUserValidationTest() throws Exception {

        // given
        UserCreateRequest request =
                new UserCreateRequest("", -1);

        // when & then
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void orderAccessDeniedTest() throws Exception {

        // given
        Long userId = 1L;
        Long orderId = 10L;

        User loginUser = new User(1L, "Kim", 25);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        loginUser,
                        null,
                        List.of()
                )
        );

        try {
            doThrow(new UserAccessDeniedException())
                    .when(userService)
                    .removeUserOrder(userId, orderId, 1L);

            // when & then
            mockMvc.perform(
                            delete(
                                    "/users/{userId}/orders/{orderId}",
                                    userId,
                                    orderId
                            )
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value(403));

            verify(userService)
                    .removeUserOrder(userId, orderId, 1L);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void userAccessDeniedTest() throws Exception {

        // given
        User loginUser = new User(2L, "Lee", 30);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        loginUser,
                        null,
                        List.of()
                )
        );

        try {
            doThrow(new UserAccessDeniedException())
                    .when(userService)
                    .deleteUser(1L, 2L);

            // when & then
            mockMvc.perform(delete("/users/1"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value(403))
                    .andExpect(jsonPath("$.message")
                            .value("해당 사용자에 대한 접근 권한이 없습니다."));

            verify(userService).deleteUser(1L, 2L);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @TestConfiguration
    static class TestSecurityConfig implements WebMvcConfigurer {

        @Override
        public void addArgumentResolvers(
                List<HandlerMethodArgumentResolver> resolvers
        ) {
            resolvers.add(
                    new AuthenticationPrincipalArgumentResolver()
            );
        }
    }
}