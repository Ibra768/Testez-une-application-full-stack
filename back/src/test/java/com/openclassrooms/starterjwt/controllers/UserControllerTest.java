package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> mockUsers;

    @BeforeEach
    public void setup() {
        this.mockUsers = new ArrayList<>();
        this.mockUsers.add(new User("john.doe@example.com", "Doe", "John","A small password", false));
        this.mockUsers.add(new User("john.dae@example.com", "Dae", "John","A small password", false));
    }

    @Test
    @WithMockUser
    public void getUserByIdTest() throws Exception {
        when(userService.findById(anyLong())).thenReturn(mockUsers.get(0));

        mockMvc.perform(get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void getUserByIdNotFoundTest() throws Exception {

        when(userService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void getUserByIdBadRequestTest() throws Exception {
        mockMvc.perform(get("/api/user/not_a_number")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    public void deleteUserByIdTest() throws Exception {
        when(userService.findById(anyLong())).thenReturn(mockUsers.get(0));

        mockMvc.perform(delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void deleteUserByIdNotFoundTest() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    public void deleteUserByIdUnauthorizedTest() throws Exception {
        User user = mockUsers.get(1);
        when(userService.findById(anyLong())).thenReturn(user);

        mockMvc.perform(delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void deleteUserByIdBadRequestTest() throws Exception {
        mockMvc.perform(delete("/api/user/not_a_number")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}