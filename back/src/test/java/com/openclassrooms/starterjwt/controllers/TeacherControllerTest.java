package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    private Teacher mockTeacher;
    private TeacherDto mockTeacherDto;

    @BeforeEach
    public void init() {
        this.mockTeacher = new Teacher(1L, "Doe", "John", null, null);
        this.mockTeacherDto = new TeacherDto(1L, "Doe", "John", null, null);
    }

    @Test
    @WithMockUser
    void findByIdTest() throws Exception {

        // GIVEN
        when(teacherService.findById(anyLong())).thenReturn(this.mockTeacher);
        when(teacherMapper.toDto(mockTeacher)).thenReturn(this.mockTeacherDto);

        // WHEN & THEN
        mockMvc.perform(get("/api/teacher/"+this.mockTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(this.mockTeacherDto)));
    }
    @Test
    @WithMockUser
    void findByIdNotFoundTest() throws Exception {
        // GIVEN
        when(teacherService.findById(anyLong())).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(get("/api/teacher/"+this.mockTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser
    void findByIdBadRequest() throws Exception {
        // GIVEN
        when(teacherService.findById(anyLong())).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(get("/api/teacher/not_a_number")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser
    void findAllTest() throws Exception {
        // GIVEN
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(this.mockTeacher);
        List<TeacherDto> teacherDtos = new ArrayList<>();
        teacherDtos.add(this.mockTeacherDto);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        // WHEN & THEN
        mockMvc.perform(get("/api/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}