package org.bookstore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bookstore.controller.UserController;
import org.bookstore.dto.UserDto;
import org.bookstore.repository.UserRepository;
import org.bookstore.response.UserResponse;
import org.bookstore.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Set;

import static org.bookstore.model.Role.ROLE_ADMIN;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddUser() throws Exception {
        // Given
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setRoles(Set.of(ROLE_ADMIN));
        userDto.setPassword("aaaa");


        UserResponse expectedResponse = new UserResponse();
        expectedResponse.setUsername("testUser");


        given(userService.create(any(UserDto.class))).willReturn(expectedResponse);

        // When
        mockMvc.perform(post("/api/v1/user/register")
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testUser"));

        // Then
        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUser() throws Exception {
        // Given
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setUsername("updatedUser");
        userDto.setRoles(Set.of(ROLE_ADMIN));
        userDto.setPassword("aaaa");

        UserResponse expectedResponse = new UserResponse();
        expectedResponse.setId(userId);
        expectedResponse.setUsername("updatedUser");


        given(userService.update(any(UserDto.class), eq(userId))).willReturn(expectedResponse);

        // When
        mockMvc.perform(put("/api/v1/user/{id}", userId)
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("updatedUser"));

        // Then
        verify(userService, times(1)).update(any(UserDto.class), eq(userId));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser() throws Exception {
        // Given
        Long userId = 1L;

        // When
        mockMvc.perform(delete("/api/v1/user/{id}", userId)
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then
        verify(userService, times(1)).delete(userId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        // Given
        Page<UserResponse> usersPage = new PageImpl<>(Collections.singletonList(new UserResponse()));
        given(userService.get(any(PageRequest.class))).willReturn(usersPage);

        // When
        mockMvc.perform(get("/api/v1/user").with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        // Then
        verify(userService, times(1)).get(any(PageRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserById() throws Exception {
        // Given
        Long userId = 1L;
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setUsername("testUser");
        userResponse.setRoles(Set.of(ROLE_ADMIN));

        given(userService.getById(userId)).willReturn(userResponse);

        // When
        mockMvc.perform(get("/api/v1/user/{id}", userId).with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("testUser"));

        // Then
        verify(userService, times(1)).getById(userId);
    }
}