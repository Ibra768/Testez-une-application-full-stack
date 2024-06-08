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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void init() {

        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    void authenticateUserTest() {
        // GIVEN
        String email = "john.doe@example.com";
        // On crée une requête de connexion avec un email et un mot de passe
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("password");

        // On crée un utilisateur mocké pour l'authentification
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        // On simule le comportement de l'utilisateur pour retourner un email
        when(userDetails.getUsername()).thenReturn(email);

        // On simule le comportement de l'authentification pour retourner un utilisateur
        when(authentication.getPrincipal()).thenReturn(userDetails);
        // On simule le comportement du gestionnaire d'authentification pour retourner une authentification
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        // On simule le comportement de JwtUtils pour générer un token JWT
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mockJwtToken");

        // On crée un utilisateur mocké
        User mockUser = new User(1L, email, "User", "USER", "password", false, LocalDateTime.now(), LocalDateTime.now());
        // On simule le comportement du dépôt d'utilisateurs pour retourner un utilisateur lorsqu'on lui passe un email
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mockUser));

        // WHEN
        // On appelle la méthode du contrôleur pour authentifier l'utilisateur
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // THEN
        // On vérifie que le statut de la réponse est OK
        // On vérifie que le corps de la réponse est une instance de JwtResponse
        // On vérifie que le token dans la réponse est égal au token mocké
        // On vérifie que le nom d'utilisateur dans la réponse est égal à l'email de l'utilisateur mocké
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(JwtResponse.class);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertThat(jwtResponse.getToken()).isEqualTo("mockJwtToken");
        assertThat(jwtResponse.getUsername()).isEqualTo(email);
    }

    @Test
    void registerUserSuccessTest() {
        // GIVEN
        // On crée une requête d'inscription avec un email, un prénom, un nom et un mot de passe
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("user@example.com");
        signUpRequest.setFirstName("User");
        signUpRequest.setLastName("USER");
        signUpRequest.setPassword("password");

        // On simule le comportement du dépôt d'utilisateurs pour retourner false lorsqu'on vérifie si un utilisateur existe par email
        when(userRepository.existsByEmail(any())).thenReturn(false);
        // On simule le comportement de l'encodeur de mot de passe pour retourner un mot de passe encodé
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // WHEN
        // On appelle la méthode du contrôleur pour inscrire l'utilisateur
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // THEN
        // On vérifie que le statut de la réponse est OK
        // On vérifie que le corps de la réponse est une instance de MessageResponse
        // On vérifie que le message dans la réponse est "User registered successfully!"
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(MessageResponse.class);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");
    }

    @Test
    void registerUserBadRequestTest() {
        // GIVEN
        // On crée une requête d'inscription invalide avec un email, un prénom, un nom et un mot de passe
        SignupRequest invalidSignUpRequest = new SignupRequest();
        invalidSignUpRequest.setEmail("user@example.com");
        invalidSignUpRequest.setFirstName("User");
        invalidSignUpRequest.setLastName("USER");
        invalidSignUpRequest.setPassword("password");

        // On simule le comportement du dépôt d'utilisateurs pour retourner true lorsqu'on vérifie si un utilisateur existe par email
        when(userRepository.existsByEmail(any())).thenReturn(true);

        // WHEN
        // On appelle la méthode du contrôleur pour inscrire l'utilisateur avec une requête invalide
        ResponseEntity<?> response = authController.registerUser(invalidSignUpRequest);

        // THEN
        // On vérifie que le statut de la réponse est BAD_REQUEST
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


}