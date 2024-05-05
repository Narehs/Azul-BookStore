package org.bookstore.controller;

import org.bookstore.dto.UserDto;
import org.bookstore.response.UserResponse;
import org.bookstore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {


    private UserController userController;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void createUser() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        UserResponse response = new UserResponse();
        when(userService.create(any(UserDto.class))).thenReturn(response);

        // When
        UserResponse createdUser = userController.createUser(userDto);

        // Then
        assertEquals(response, createdUser);
        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    void updateUser() {
        // Given
        Long userId = 123L;
        UserDto userDto = new UserDto();
        UserResponse response = new UserResponse();
        when(userService.update(userDto, userId)).thenReturn(response);

        // When
        UserResponse updatedUser = userController.updateUser(userDto, userId);

        // Then
        assertEquals(response, updatedUser);
        verify(userService, times(1)).update(userDto, userId);
    }

    @Test
    void deleteUser() {
        // Given
        Long userId = 123L;

        // When
        userController.deleteUser(userId);

        // Then
        verify(userService, times(1)).delete(userId);
    }

    @Test
    void getAllUsers() {
        // Given
        Page<UserResponse> expectedPage = new PageImpl<>(Collections.emptyList());
        int page = 0;
        int size = 10;
        when(userService.get(PageRequest.of(page, size))).thenReturn(expectedPage);

        // When
        Page<UserResponse> actualPage = userController.getAllUsers(page, size);

        // Then
        assertEquals(expectedPage, actualPage);
        verify(userService, times(1)).get(PageRequest.of(page, size));
    }

    @Test
    void getUserById() {
        // Given
        Long userId = 123L;
        UserResponse response = new UserResponse();
        response.setId(userId);
        when(userService.getById(userId)).thenReturn(response);

        // When
        UserResponse actualResponse = userController.getUserById(userId);

        // Then
        assertEquals(response, actualResponse);
        verify(userService, times(1)).getById(userId);
    }
}

