package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TeacherRepository teacherRepository;

    @Nested
    public class FindById {

        @Test
        @WithMockUser
        void shouldHaveId() throws Exception {

            // GIVEN
            when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(new Teacher()));

            // WHEN & THEN
            mockMvc.perform(get("/api/teacher/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(new ObjectMapper().writeValueAsString(new TeacherDto())));

            verify(teacherRepository,times(1)).findById(anyLong());
        }

        @Test
        @WithMockUser
        void shouldNotFound() throws Exception {

            // GIVEN
            when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

            // WHEN & THEN
            mockMvc.perform(get("/api/teacher/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(teacherRepository, times(1)).findById(anyLong());
        }
        @Test
        @WithMockUser
        void shouldBadRequest() throws Exception {

            // WHEN & THEN
            mockMvc.perform(get("/api/teacher/not_a_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(teacherRepository, times(0)).findById(anyLong());

        }
    }
    @Test
    @WithMockUser
    void findAllTest() throws Exception {
        ArrayList<Teacher> teachers = new ArrayList<>();
        ArrayList<TeacherDto> teachersDto = new ArrayList<>();
        // GIVEN
        when(teacherRepository.findAll()).thenReturn(teachers);

        // WHEN & THEN
        mockMvc.perform(get("/api/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(teachersDto)));

        verify(teacherRepository, times(1)).findAll();
    }
}