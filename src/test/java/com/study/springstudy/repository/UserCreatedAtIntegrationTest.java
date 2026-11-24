package com.study.springstudy.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import jakarta.persistence.EntityManager;
import com.study.springstudy.domain.User;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("flywaytest")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class UserCreatedAtIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void createdAtIsGeneratedByDatabase() {

        // given
        String email = UUID.randomUUID() + "@example.com";

        User user = new User("testUser", 25, email, "testPassword");

        // when
        User savedUser = userRepository.save(user);

        entityManager.flush();
        entityManager.clear();

        User foundUser = userRepository.findById(savedUser.getId())
                .orElseThrow();

        // then
        assertThat(foundUser.getCreatedAt()).isNotNull();
    }
}