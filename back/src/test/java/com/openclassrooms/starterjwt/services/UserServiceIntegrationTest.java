package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest()
class UserServiceIntegrationTest {

    @Autowired()
    private UserRepository userRepository;

    @Autowired()
    private UserService userService;

    @Test
    void deleteTest() {

        // GIVEN
        // On supprime John Doe
        Long userId = 2L;

        // WHEN
        userService.delete(userId);

        // THEN
        // On vérifie que l'utilisateur John Doe n'existe plus
        assertThat(userRepository.existsById(userId)).isFalse();

    }

    @Test
    void findOneByExistingIdTest() {

        // GIVEN
        Long userId = 3L;

        // WHEN
        User result = userService.findById(userId);

        // THEN
        assertThat(result.getFirstName()).isEqualTo("Lorem");
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getEmail()).isEqualTo("lorem.ipsum@example.com");
        assertThat(result.getLastName()).isEqualTo("Ipsum");
        assertThat(result.getPassword()).isEqualTo("A complex password");

    }

    @Test
    void findOneByNonExistingIdTest() {

        // GIVEN
        Long userId = 0L;

        // WHEN
        User result = userService.findById(userId);

        // THEN
        assertThat(result).isNull();
    }

}