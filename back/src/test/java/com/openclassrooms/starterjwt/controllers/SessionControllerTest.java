package com.openclassrooms.starterjwt.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Teacher mockTeacher;
    private List<User> mockUsers;
    private List<Session> mockSessions;
    private List<SessionDto> mockSessionsDto;

    @BeforeEach
    public void setup() {
        this.mockTeacher = new Teacher(1L, "Doe", "John", null, null);
        this.mockUsers = new ArrayList<>();
        this.mockUsers.add(new User("john.doe@example.com", "Doe", "John","A small password", false));
        this.mockSessions = new ArrayList<>();
        this.mockSessions.add(new Session(1L, "First session", new Date(), "A small description", this.mockTeacher, this.mockUsers, null, null));
        this.mockSessions.add(new Session(2L, "Second session", new Date(), "A small description", this.mockTeacher, this.mockUsers, null, null));
        this.mockSessionsDto = new ArrayList<>();
        this.mockSessionsDto.add(new SessionDto(1L, "First session", new Date(), this.mockTeacher.getId(), "A small description", new ArrayList<>(), null, null));
        this.mockSessionsDto.add(new SessionDto(2L, "Second session", new Date(), this.mockTeacher.getId(), "A small description", new ArrayList<>(), null, null));
    }

    @Test
    @WithMockUser
    void findByIdTest() throws Exception {

        // GIVEN
        Session mockSession = this.mockSessions.get(0);
        SessionDto mockSessionDto = this.mockSessionsDto.get(0);
        when(sessionService.getById(anyLong())).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        // WHEN & THEN
        mockMvc.perform(get("/api/session/"+mockSession.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void findByIdNotFoundTest() throws Exception {

        // GIVEN
        when(sessionService.getById(anyLong())).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void findByIdBadRequest() throws Exception {

        // GIVEN
        when(sessionService.getById(anyLong())).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(get("/api/session/not_a_number")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void findAllSuccessTest() throws Exception {

        // GIVEN
        when(sessionService.findAll()).thenReturn(mockSessions);
        when(sessionMapper.toDto(mockSessions)).thenReturn(mockSessionsDto);

        // WHEN & THEN
        mockMvc.perform(get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void createSuccessTest() throws Exception {
        // GIVEN
        SessionDto mockSessionDto = mockSessionsDto.get(0);
        mockSessionDto.setDate(new Date());
        Session mockSession = mockSessions.get(0);


        when(sessionMapper.toEntity(mockSessionDto)).thenReturn(mockSession);
        when(sessionService.create(mockSession)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        mockMvc.perform(post("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockSessionDto)))
                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser
    void updateSuccessTest() throws Exception {
        // GIVEN
        SessionDto mockSessionDto = mockSessionsDto.get(0);
        Session mockSession = mockSessions.get(0);

        when(sessionMapper.toEntity(mockSessionDto)).thenReturn(mockSession);
        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String mockSessionDtoJson = objectMapper.writeValueAsString(mockSessionDto);

        // WHEN & THEN
        mockMvc.perform(put("/api/session/" + mockSessionDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockSessionDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteSuccessTest() throws Exception {
        // GIVEN
        Session mockSession = mockSessions.get(0);
        when(sessionService.getById(anyLong())).thenReturn(mockSession);

        // WHEN & THEN
        mockMvc.perform(delete("/api/session/" + mockSession.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).delete(mockSession.getId());
    }

    @Test
    @WithMockUser
    void deleteNotFoundTest() throws Exception {
        // GIVEN
        when(sessionService.getById(anyLong())).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteBadRequestTest() throws Exception {
        // WHEN & THEN
        mockMvc.perform(delete("/api/session/not_a_number")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void participateSuccessTest() throws Exception {
        // GIVEN
        String sessionId = "1";
        String userId = "1";

        // WHEN & THEN
        mockMvc.perform(post("/api/session/" + sessionId + "/participate/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).participate(Long.parseLong(sessionId), Long.parseLong(userId));
    }

    @Test
    @WithMockUser
    void participateBadRequestTest() throws Exception {
        // GIVEN
        String invalidSessionId = "not_a_number";
        String userId = "1";

        // WHEN & THEN
        mockMvc.perform(post("/api/session/" + invalidSessionId + "/participate/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void noLongerParticipateSuccessTest() throws Exception {
        // GIVEN
        String sessionId = "1";
        String userId = "1";

        // WHEN & THEN
        mockMvc.perform(delete("/api/session/" + sessionId + "/participate/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).noLongerParticipate(Long.parseLong(sessionId), Long.parseLong(userId));
    }

    @Test
    @WithMockUser
    void noLongerParticipateBadRequestTest() throws Exception {
        // GIVEN
        String invalidSessionId = "not_a_number";
        String userId = "1";

        // WHEN & THEN
        mockMvc.perform(delete("/api/session/" + invalidSessionId + "/participate/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}