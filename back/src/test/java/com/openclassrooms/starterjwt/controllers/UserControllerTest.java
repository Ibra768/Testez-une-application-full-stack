package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository $userRepository;

    @Nested
    public class FindById {

        @Test
        @WithMockUser
        void shouldHaveId() throws Exception {

            UserDto userDto = new UserDto(1L,"example@mail.com","Doe","John",true,"Password",null,null);
            User user = new User(1L,"example@mail.com","Doe","John","Password",true,null,null);
            // GIVEN
            when($userRepository.findById(anyLong())).thenReturn(Optional.of(user));

            // WHEN & THEN
            mockMvc.perform(get("/api/user/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(new ObjectMapper().writeValueAsString(userDto)));

            verify($userRepository,times(1)).findById(anyLong());
        }

        @Test
        @WithMockUser
        void shouldNotFound() throws Exception {

            // GIVEN
            when($userRepository.findById(anyLong())).thenReturn(Optional.empty());

            // WHEN & THEN
            mockMvc.perform(get("/api/user/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify($userRepository, times(1)).findById(anyLong());
        }
        @Test
        @WithMockUser
        void shouldBadRequest() throws Exception {

            // WHEN & THEN
            mockMvc.perform(get("/api/user/not_a_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify($userRepository, times(0)).findById(anyLong());

        }
    }

    @Nested
    public class Save{
        @Test
        @WithMockUser(username = "example@mail.com", roles = {"USER"})
        void shouldSave() throws Exception {

            User user = new User(1L,"example@mail.com","Doe","John","Password",true,null,null);
            // GIVEN
            when($userRepository.findById(anyLong())).thenReturn(Optional.of(user));

            // WHEN & THEN
            mockMvc.perform(delete("/api/user/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify($userRepository,times(1)).findById(anyLong());
            verify($userRepository,times(1)).deleteById(anyLong());
        }

        @Test
        @WithMockUser
        void shouldNotFound() throws Exception {

            // GIVEN
            when($userRepository.findById(anyLong())).thenReturn(Optional.empty());

            // WHEN & THEN
            mockMvc.perform(delete("/api/user/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify($userRepository,times(1)).findById(anyLong());
            verify($userRepository,times(0)).deleteById(anyLong());
        }
        @Test
        @WithMockUser
        void shouldUnauthorized() throws Exception {

            User user = new User(1L,"example@mail.com","Doe","John","Password",true,null,null);
            // GIVEN
            when($userRepository.findById(anyLong())).thenReturn(Optional.of(user));

            // Arrange
            UserDetails mockUserDetails = org.springframework.security.core.userdetails.User.withUsername("example@mail.com").password("password").roles("USER").build();
            Authentication mockAuthentication = mock(Authentication.class);
            SecurityContext mockSecurityContext = mock(SecurityContext.class);

            when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
            when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

            SecurityContextHolder.setContext(mockSecurityContext);

            // WHEN & THEN
            mockMvc.perform(delete("/api/user/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());

            verify($userRepository,times(1)).findById(anyLong());
            verify($userRepository,times(0)).deleteById(anyLong());

        }
        void shouldBadRequest() throws Exception {

            // WHEN & THEN
            mockMvc.perform(delete("/api/user/not_a_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify($userRepository, times(0)).findById(anyLong());

        }
    }

}