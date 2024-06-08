package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;

@SpringBootTest()
class TeacherServiceIntegrationTest {

    @Autowired()
    private TeacherService teacherService;

    @Test
    void findAllTest() {

        // WHEN
        List<Teacher> result = teacherService.findAll();

        // THEN
        assertThat(result.get(0).getFirstName()).isEqualTo("Margot");
        assertThat(result.get(0).getLastName()).isEqualTo("DELAHAYE");
        assertThat(result.get(1).getFirstName()).isEqualTo("Hélène");
        assertThat(result.get(1).getLastName()).isEqualTo("THIERCELIN");
        // Ajouté à la BDD pour le test
        assertThat(result.get(2).getFirstName()).isEqualTo("John");
        assertThat(result.get(2).getLastName()).isEqualTo("Doe");

    }

    @Test
    void findOneByExistingIdTest() {

        // GIVEN
        // Ajouté à la BDD pour le test
        Long teacherId = 3L;

        // WHEN
        Teacher result = teacherService.findById(teacherId);

        // THEN
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
    }

    @Test
    void findOneByNonExistingIdTest() {

        // GIVEN
        Long teacherId = 0L;

        // WHEN
        Teacher result = teacherService.findById(teacherId);

        // THEN
        assertThat(result).isNull();
    }

}