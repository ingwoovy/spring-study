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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        UserResponse response = new UserResponse(1L,"Kim",25);

        when(userService.createUser(any(UserCreateRequest.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                    {
                        "name": "Kim",
                        "age": 25
                    }
                    """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Kim"))
                .andExpect(jsonPath("$.age").value(25));
    }
}