package com.openclassrooms.starterjwt.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private Session mockSession;

    @BeforeEach
    public void setup() {
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User(1L, "john.doe@example.com", "Doe", "John", "A simple password", false, LocalDateTime.now(), LocalDateTime.now()));
        mockUsers.add(new User(2L, "lorem.ipsum@example.com", "Ipsum", "Lorem", "A simple password", true, LocalDateTime.now(), LocalDateTime.now()));

        Teacher mockTeacher = new Teacher(1L, "Johnson", "Carl", LocalDateTime.now(), LocalDateTime.now());

        this.mockSession = new Session(1L, "First session", new Date(), "A small description", mockTeacher, mockUsers, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void findByIdSuccessTest() {
        // GIVEN
        // On définit un identifiant de session
        String sessionId = "1";
        // On crée une session mockée
        Session session = this.mockSession;
        // On simule le comportement du service pour retourner une session lorsqu'on lui passe un identifiant
        when(sessionService.getById(anyLong())).thenReturn(session);

        // WHEN
        // On appelle la méthode du contrôleur pour trouver la session par son identifiant
        ResponseEntity<?> response = sessionController.findById(sessionId);

        // THEN
        // On vérifie que le statut de la réponse est OK
        // On vérifie que le corps de la réponse est égal à la session mockée convertie en DTO
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(this.sessionMapper.toDto(session));
    }

    @Test
    void findByIdNotFoundTest() {
        // GIVEN
        // On définit un identifiant de session
        String sessionId = "1";
        // On simule le comportement du service pour retourner null lorsqu'on lui passe un identifiant
        when(sessionService.getById(anyLong())).thenReturn(null);

        // WHEN
        // On appelle la méthode du contrôleur pour trouver la session par son identifiant
        ResponseEntity<?> response = sessionController.findById(sessionId);

        // THEN
        // On vérifie que le statut de la réponse est NOT_FOUND
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findByIdBadRequestTest() {
        // GIVEN
        // On définit un identifiant de session invalide
        String invalidSessionId = "An invalid ID";

        // WHEN
        // On appelle la méthode du contrôleur pour trouver la session par son identifiant
        ResponseEntity<?> response = sessionController.findById(invalidSessionId);

        // THEN
        // On vérifie que le statut de la réponse est BAD_REQUEST
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void findAllSuccessTest() {
        // GIVEN
        // On crée une liste de sessions mockées
        List<Session> sessions = List.of(this.mockSession);
        // On simule le comportement du service pour retourner toutes les sessions
        when(sessionService.findAll()).thenReturn(sessions);

        // WHEN
        // On appelle la méthode du contrôleur pour récupérer toutes les sessions
        ResponseEntity<?> response = sessionController.findAll();

        // THEN
        // On vérifie que le statut de la réponse est OK
        // On vérifie que le corps de la réponse est égal à la liste des sessions mockées converties en DTO
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(this.sessionMapper.toDto(sessions));
    }

    @Test
    void createSuccessTest() {
        // GIVEN
        // On crée un DTO de session
        SessionDto sessionDto = new SessionDto();
        // On crée une session mockée
        Session session = this.mockSession;
        // On simule le comportement du mapper pour convertir le DTO en entité
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        // On simule le comportement du service pour créer la session
        when(sessionService.create(session)).thenReturn(session);
        // On simule le comportement du mapper pour convertir l'entité en DTO
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // WHEN
        // On appelle la méthode du contrôleur pour créer la session
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // THEN
        // On vérifie que le statut de la réponse est OK
        // On vérifie que le corps de la réponse est égal au DTO de la session mockée
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(this.sessionMapper.toDto(session));
    }

    @Test
    void updateSuccessTest() {
        // GIVEN
        // On définit un identifiant de session
        String sessionId = "1";
        // On crée un DTO de session
        SessionDto sessionDto = new SessionDto();
        // On crée une session mockée
        Session session = this.mockSession;
        // On simule le comportement du mapper pour convertir le DTO en entité
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        // On simule le comportement du service pour mettre à jour la session
        when(sessionService.update(anyLong(), eq(session))).thenReturn(session);
        // On simule le comportement du mapper pour convertir l'entité en DTO
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // WHEN
        // On appelle la méthode du contrôleur pour mettre à jour la session
        ResponseEntity<?> response = sessionController.update(sessionId, sessionDto);

        // THEN
        // On vérifie que le statut de la réponse est OK
        // On vérifie que le corps de la réponse est égal au DTO de la session mockée
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(this.sessionMapper.toDto(session));
    }

    @Test
    void updateBadRequestTest() {
        // GIVEN
        // On définit un identifiant de session invalide
        String invalidSessionId = "An invalid ID";
        // On crée un DTO de session
        SessionDto sessionDto = new SessionDto();

        // WHEN
        // On appelle la méthode du contrôleur pour mettre à jour la session avec un identifiant invalide
        ResponseEntity<?> response = sessionController.update(invalidSessionId, sessionDto);

        // THEN
        // On vérifie que le statut de la réponse est BAD_REQUEST
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteSuccessTest() {
        // GIVEN
        // On définit un identifiant de session
        String sessionId = "1";
        // On crée une session mockée
        Session session = this.mockSession;
        // On simule le comportement du service pour retourner une session lorsqu'on lui passe un identifiant
        when(sessionService.getById(anyLong())).thenReturn(session);

        // WHEN
        // On appelle la méthode du contrôleur pour supprimer la session
        ResponseEntity<?> response = sessionController.save(sessionId);

        // THEN
        // On vérifie que le statut de la réponse est OK
        // On vérifie que le service a été appelé une fois pour supprimer la session
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).delete(Long.parseLong(sessionId));
    }

    @Test
    void deleteNotFoundTest() {
        // GIVEN
        // On définit un identifiant de session
        String invalidSessionId = "1";

        // WHEN
        // On appelle la méthode du contrôleur pour supprimer la session avec un identifiant invalide
        ResponseEntity<?> response = sessionController.save(invalidSessionId);

        // THEN
        // On vérifie que le statut de la réponse est NOT_FOUND
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBadRequestTest() {
        // GIVEN
        // On définit un identifiant de session invalide
        String invalidSessionId = "An invalid ID";

        // WHEN
        // On appelle la méthode du contrôleur pour supprimer la session avec un identifiant invalide
        ResponseEntity<?> response = sessionController.save(invalidSessionId);

        // THEN
        // On vérifie que le statut de la réponse est BAD_REQUEST
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void participateSuccessTest() {
        // GIVEN
        // On définit un identifiant de session et un identifiant d'utilisateur
        String sessionId = "1";
        String userId = "1";

        // WHEN
        // On appelle la méthode du contrôleur pour faire participer l'utilisateur à la session
        ResponseEntity<?> response = sessionController.participate(sessionId, userId);

        // THEN
        // On vérifie que le statut de la réponse est OK
        // On vérifie que le service a été appelé une fois pour faire participer l'utilisateur à la session
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).participate(Long.parseLong(sessionId), Long.parseLong(userId));
    }

    @Test
    void participateBadRequestTest() {
        // GIVEN
        // On définit un identifiant de session et un identifiant d'utilisateur invalides
        String invalidSessionId = "An invalid ID";
        String userId = "1";

        // WHEN
        // On appelle la méthode du contrôleur pour faire participer l'utilisateur à la session avec un identifiant de session invalide
        ResponseEntity<?> response = sessionController.participate(invalidSessionId, userId);

        // THEN
        // On vérifie que le statut de la réponse est BAD_REQUEST
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void noLongerParticipateSuccessTest() {
        // GIVEN
        // On définit un identifiant de session et un identifiant d'utilisateur
        String sessionId = "1";
        String userId = "1";

        // WHEN
        // On appelle la méthode du contrôleur pour faire cesser la participation de l'utilisateur à la session
        ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId, userId);

        // THEN
        // On vérifie que le statut de la réponse est OK
        // On vérifie que le service a été appelé une fois pour faire cesser la participation de l'utilisateur à la session
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).noLongerParticipate(Long.parseLong(sessionId), Long.parseLong(userId));
    }

    @Test
    void noLongerParticipateBadRequestTest() {
        // GIVEN
        // On définit un identifiant de session et un identifiant d'utilisateur invalides
        String invalidSessionId = "An invalid ID";
        String userId = "1";

        // WHEN
        // On appelle la méthode du contrôleur pour faire cesser la participation de l'utilisateur à la session avec un identifiant de session invalide
        ResponseEntity<?> response = sessionController.noLongerParticipate(invalidSessionId, userId);

        // THEN
        // On vérifie que le statut de la réponse est BAD_REQUEST
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}