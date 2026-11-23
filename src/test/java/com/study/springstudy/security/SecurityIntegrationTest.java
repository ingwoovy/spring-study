package com.study.springstudy.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import com.study.springstudy.global.jwt.JwtUtil;
import com.study.springstudy.repository.UserRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.study.springstudy.domain.User;
import com.study.springstudy.domain.Role;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void adminApiWithoutTokenReturns401() throws Exception {

        // when & then
        mockMvc.perform(get("/admin/test"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminApiWithUserRoleReturns403() throws Exception {

        mockMvc.perform(
                        get("/admin/test")
                                .with(user("testuser").roles("USER"))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void adminApiWithAdminRoleReturns200() throws Exception {

        // when & then
        mockMvc.perform(
                        get("/admin/test")
                                .with(user("adminuser").roles("ADMIN"))
                )
                .andExpect(status().isOk());
    }

    @Test
    void adminApiWithInvalidJwtReturns401() throws Exception {

        // when & then
        mockMvc.perform(
                        get("/admin/test")
                                .header("Authorization", "Bearer invalid.jwt.token")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminApiWithValidJwtReturns200() throws Exception {

        // given
        String email = "admin-test@example.com";

        User admin = mock(User.class);

        when(admin.getRole())
                .thenReturn(Role.ROLE_ADMIN);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(admin));

        // when
        String token = jwtUtil.generateToken(email);

        // then
        mockMvc.perform(
                        get("/admin/test")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @Test
    void adminApiWithDeletedUserReturns401() throws Exception {

        // given
        String email = "deleted@example.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // when
        String token = jwtUtil.generateToken(email);

        // then
        mockMvc.perform(
                        get("/admin/test")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminApiWithValidUserJwtReturns403() throws Exception {

        // given
        String email = "user-test@example.com";

        User normalUser = mock(User.class);

        when(normalUser.getRole())
                .thenReturn(Role.ROLE_USER);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(normalUser));

        // when
        String token = jwtUtil.generateToken(email);

        // then
        mockMvc.perform(
                        get("/admin/test")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }
}