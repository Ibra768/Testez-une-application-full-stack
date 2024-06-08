package com.openclassrooms.starterjwt.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher mockTeacher;

    @BeforeEach
    public void init() {
        this.mockTeacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void findByIdSuccessTest() {

        // GIVEN
        String teacherId = "1";
        Teacher teacher = this.mockTeacher;
        // On simule le comportement du service pour retourner un enseignant lorsqu'on lui passe un identifiant
        when(teacherService.findById(anyLong())).thenReturn(teacher);

        // WHEN
        // On appelle la méthode du contrôleur avec l'identifiant de l'enseignant
        ResponseEntity<?> response = teacherController.findById(teacherId);

        // THEN
        // On vérifie que le statut de la réponse est OK et que le corps de la réponse correspond à l'enseignant attendu
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(this.teacherMapper.toDto(teacher));

    }
    @Test
    void findByIdNotFoundTest() {

        // GIVEN
        String teacherId = "1";
        // On simule le comportement du service pour retourner null lorsqu'on lui passe un identifiant
        when(teacherService.findById(anyLong())).thenReturn(null);

        // WHEN
        // On appelle la méthode du contrôleur avec l'identifiant de l'enseignant
        ResponseEntity<?> response = teacherController.findById(teacherId);

        // THEN
        // On vérifie que le statut de la réponse est NOT_FOUND
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void findByIdBadRequestTest() {

        // GIVEN
        String invalidTeacherId = "Invalid ID";

        // WHEN
        // On appelle la méthode du contrôleur avec un identifiant invalide
        ResponseEntity<?> response = teacherController.findById(invalidTeacherId);

        // THEN
        // On vérifie que le statut de la réponse est BAD_REQUEST
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void findAllSuccessTest() {

        // GIVEN
        List<Teacher> teachers = List.of(this.mockTeacher);
        // On simule le comportement du service pour retourner une liste d'enseignants
        when(teacherService.findAll()).thenReturn(teachers);

        // WHEN
        // On appelle la méthode du contrôleur pour obtenir tous les enseignants
        ResponseEntity<?> response = teacherController.findAll();

        // THEN
        // On vérifie que le statut de la réponse est OK et que le corps de la réponse correspond à la liste d'enseignants attendue
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(this.teacherMapper.toDto(teachers));

    }


}