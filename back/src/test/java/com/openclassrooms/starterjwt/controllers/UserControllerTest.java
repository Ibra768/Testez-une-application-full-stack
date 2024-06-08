package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    public void setup() {

        SecurityContextHolder.setContext(securityContext);

        this.mockUser = new User(1L, "john.doe@example.com", "Doe", "John", "A complex password", false, LocalDateTime.now(), LocalDateTime.now());

    }

    @Test
    void findByIdSuccessTest() {

        // GIVEN
        String userId = "1";
        User user = this.mockUser;
        UserDto userDto = new UserDto();
        // On simule le comportement du service pour retourner un utilisateur lorsqu'on lui passe un identifiant
        when(userService.findById(anyLong())).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // WHEN
        // On appelle la méthode du contrôleur avec l'identifiant de l'utilisateur
        ResponseEntity<?> response = userController.findById(userId);

        // THEN
        // On vérifie que le statut de la réponse est OK et que le corps de la réponse correspond à l'utilisateur attendu
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(this.userMapper.toDto(user));
    }

    @Test
    void findByIdNotFoundTest() {

        // GIVEN
        String userId = "1";
        // On simule le comportement du service pour retourner null lorsqu'on lui passe un identifiant
        when(userService.findById(anyLong())).thenReturn(null);

        // WHEN
        // On appelle la méthode du contrôleur avec l'identifiant de l'utilisateur
        ResponseEntity<?> response = userController.findById(userId);

        // THEN
        // On vérifie que le statut de la réponse est NOT_FOUND
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findByIdBadRequestTest() {

        // GIVEN
        String invalidUserId = "invalidId";

        // WHEN
        // On appelle la méthode du contrôleur avec un identifiant invalide
        ResponseEntity<?> response = userController.findById(invalidUserId);

        // THEN
        // On vérifie que le statut de la réponse est BAD_REQUEST
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteSuccessTest() {

        // GIVEN
        String userId = "1";
        User mockUser = this.mockUser;

        // On simule le comportement du service pour retourner un utilisateur lorsqu'on lui passe un identifiant
        when(userService.findById(mockUser.getId())).thenReturn(mockUser);
        UserDetails mockUserDetails = mock(UserDetails.class);
        // On simule le comportement du contexte de sécurité pour retourner un utilisateur authentifié
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(mockUserDetails, null));
        when(mockUserDetails.getUsername()).thenReturn(mockUser.getEmail());

        // WHEN
        // On appelle la méthode du contrôleur pour supprimer l'utilisateur
        ResponseEntity<?> response = userController.save(userId.toString());

        // THEN
        // On vérifie que le statut de la réponse est OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // On vérifie que la méthode de suppression du service a été appelée une fois
        verify(userService, times(1)).delete(Long.parseLong(userId));
    }

    @Test
    void deleteNotFoundTest() {
        // GIVEN
        String userId = "1";
        // On simule le comportement du service pour retourner null lorsqu'on lui passe un identifiant
        when(userService.findById(anyLong())).thenReturn(null);

        // WHEN
        // On appelle la méthode du contrôleur pour supprimer l'utilisateur
        ResponseEntity<?> response = userController.save(userId);

        // THEN
        // On vérifie que le statut de la réponse est NOT_FOUND
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteUnauthorizedTest() {

        // GIVEN
        String userId = "1";
        User mockUser = this.mockUser;

        // On simule le comportement du service pour retourner un utilisateur lorsqu'on lui passe un identifiant
        when(userService.findById(mockUser.getId())).thenReturn(mockUser);
        UserDetails mockUserDetails = mock(UserDetails.class);
        // On simule le comportement du contexte de sécurité pour retourner un utilisateur authentifié
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(mockUserDetails, null));
        when(mockUserDetails.getUsername()).thenReturn("doe.john@example.com");

        // WHEN
        // On appelle la méthode du contrôleur pour supprimer l'utilisateur
        ResponseEntity<?> response = userController.save(userId.toString());

        // THEN
        // On vérifie que le statut de la réponse est UNAUTHORIZED
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void deleteBadRequestTest() {
        // GIVEN
        String invalidUserId = "Invalid ID";

        // WHEN
        // On appelle la méthode du contrôleur pour supprimer l'utilisateur avec un identifiant invalide
        ResponseEntity<?> response = userController.save(invalidUserId);

        // THEN
        // On vérifie que le statut de la réponse est BAD_REQUEST
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}