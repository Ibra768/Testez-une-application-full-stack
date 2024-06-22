package com.openclassrooms.starterjwt.controllers;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;

import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.openclassrooms.starterjwt.models.Session;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SessionRepository sessionRepository;

    @MockBean
    private UserRepository userRepository;


    @Nested
    public class FindById {

        @Test
        @WithMockUser
        void shouldHaveId() throws Exception {

            Session session = new Session();
            SessionDto sessionDto = new SessionDto();

            ArrayList<User> users = new ArrayList<>();

            Teacher teacher = new Teacher();
            TeacherDto teacherDto = new TeacherDto();

            session.setUsers(users);
            session.setTeacher(teacher);

            sessionDto.setUsers(new ArrayList<>());
            sessionDto.setTeacher_id(teacherDto.getId());

            // GIVEN
            when(sessionRepository.findById(Long.valueOf("1"))).thenReturn(Optional.of(session));

            // WHEN & THEN
            mockMvc.perform(get("/api/session/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(new ObjectMapper().writeValueAsString(sessionDto)));

            verify(sessionRepository,times(1)).findById(Long.valueOf("1"));
        }

        @Test
        @WithMockUser
        void shouldNotFound() throws Exception {

            // GIVEN
            when(sessionRepository.findById(Long.valueOf("1"))).thenReturn(Optional.empty());

            // WHEN & THEN
            mockMvc.perform(get("/api/session/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(sessionRepository, times(1)).findById(Long.valueOf("1"));
        }
        @Test
        @WithMockUser
        void shouldBadRequest() throws Exception {

            // WHEN & THEN
            mockMvc.perform(get("/api/session/not_a_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

        }
    }

    @Test
    @WithMockUser
    void findAllTest() throws Exception {
        ArrayList<Session> sessions = new ArrayList<>();
        ArrayList<SessionDto> sessionsDto = new ArrayList<>();
        // GIVEN
        when(sessionRepository.findAll()).thenReturn(sessions);

        // WHEN & THEN
        mockMvc.perform(get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(sessionsDto)));

        verify(sessionRepository, times(1)).findAll();
    }

    void createTest() throws Exception{

        // GIVEN
        when(sessionRepository.save(new Session())).thenReturn(new Session());

        mockMvc.perform(post("/api/session")
                        .content(new ObjectMapper().writeValueAsString(new SessionDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(new SessionDto())));

        verify(sessionRepository, times(1)).findAll();
        verify(sessionRepository, times(1)).deleteById(Long.valueOf("1"));
    }

    @Nested
    public class updateTest {

        @Test
        @WithMockUser
        void shouldUpdate() throws Exception {

            SessionDto sessionDto = new SessionDto();
            sessionDto.setName("name");
            sessionDto.setDate(new Date());
            sessionDto.setDescription("description");
            sessionDto.setTeacher_id(Long.valueOf("1"));
            // GIVEN
            when(sessionRepository.save(any(Session.class))).thenReturn(new Session());

            // WHEN & THEN
            mockMvc.perform(put("/api/session/1")
                            .content(new ObjectMapper().writeValueAsString(sessionDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(sessionRepository, times(1)).save(any(Session.class));
        }

        @Test
        @WithMockUser
        void shouldBadRequest() throws Exception {

            // WHEN & THEN
            mockMvc.perform(put("/api/session/not_a_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

        }
    }

    @Nested
    public class saveTest {

        @Test
        @WithMockUser
        void shouldSave() throws Exception {

            // GIVEN
            when(sessionRepository.findById(Long.valueOf("1"))).thenReturn(Optional.of(new Session()));

            // WHEN & THEN
            mockMvc.perform(delete("/api/session/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(sessionRepository,times(1)).findById(Long.valueOf("1"));
            verify(sessionRepository, times(1)).deleteById(Long.valueOf("1"));
        }

        @Test
        @WithMockUser
        void shouldNotFound() throws Exception {

            // GIVEN
            when(sessionRepository.getById(Long.valueOf("1"))).thenReturn(null);

            // WHEN & THEN
            mockMvc.perform(delete("/api/session/1")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isNotFound());

            verify(sessionRepository,times(1)).findById(Long.valueOf("1"));
            verify(sessionRepository, times(0)).deleteById(Long.valueOf("1"));

        }

        @Test
        @WithMockUser
        void shouldBadRequest() throws Exception {

            // WHEN & THEN
            mockMvc.perform(delete("/api/session/not_a_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(sessionRepository, times(0)).deleteById(Long.valueOf("1"));


        }
    }

    @Nested
    public class participateTest {

        @Test
        @WithMockUser
        void shouldParticipate() throws Exception {

            Session session = new Session();
            ArrayList<User> users = new ArrayList<>();
            User user = new User();
            user.setId(Long.valueOf("3"));
            users.add(user);
            session.setUsers(users);
            // GIVEN
            when(sessionRepository.findById(Long.valueOf("1"))).thenReturn(Optional.of(session));
            when(userRepository.findById(Long.valueOf("2"))).thenReturn(Optional.of(users.get(0)));
            when(sessionRepository.save(session)).thenReturn(session);

            // WHEN & THEN
            mockMvc.perform(post("/api/session/1/participate/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(sessionRepository,times(1)).findById(Long.valueOf("1"));
            verify(userRepository,times(1)).findById(Long.valueOf("2"));
            verify(sessionRepository, times(1)).save(any(Session.class));
        }

        @Test
        @WithMockUser
        void shouldBadRequest() throws Exception {

            // WHEN & THEN
            mockMvc.perform(post("/api/session/1/participate/not_a_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(sessionRepository,times(0)).findById(Long.valueOf("1"));
            verify(userRepository,times(0)).findById(Long.valueOf("2"));
            verify(sessionRepository, times(0)).save(any(Session.class));


        }
    }

    @Nested
    public class noLongerParticipateTest {

        @Test
        @WithMockUser
        void shouldNotParticipate() throws Exception {

            Session session = new Session();
            ArrayList<User> users = new ArrayList<>();
            User user = new User();
            user.setId(Long.valueOf("2"));
            users.add(user);
            session.setUsers(users);
            // GIVEN
            when(sessionRepository.findById(Long.valueOf("1"))).thenReturn(Optional.of(session));
            when(sessionRepository.save(session)).thenReturn(session);

            // WHEN & THEN
            mockMvc.perform(delete("/api/session/1/participate/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(sessionRepository,times(1)).findById(Long.valueOf("1"));
            verify(sessionRepository, times(1)).save(any(Session.class));
        }

        @Test
        @WithMockUser
        void shouldBadRequest() throws Exception {

            // WHEN & THEN
            mockMvc.perform(delete("/api/session/1/participate/not_a_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(sessionRepository,times(0)).findById(Long.valueOf("1"));
            verify(userRepository,times(0)).findById(Long.valueOf("2"));
            verify(sessionRepository, times(0)).save(any(Session.class));


        }
    }

}