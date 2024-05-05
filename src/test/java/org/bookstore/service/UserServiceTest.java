package org.bookstore.service;

import org.bookstore.dto.UserDto;
import org.bookstore.exception.UserNotFoundException;
import org.bookstore.mapper.UserMapper;
import org.bookstore.model.User;
import org.bookstore.repository.UserRepository;
import org.bookstore.response.UserResponse;
import org.bookstore.service.impl.UserServiceimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceimpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsers() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<User> userPage = Page.empty();
        when(userRepository.findAll(pageRequest)).thenReturn(userPage);
        Page<UserResponse> expectedPage = Page.empty();
        when(userMapper.toResponsePage(userPage)).thenReturn(expectedPage);

        // When
        Page<UserResponse> result = userService.get(pageRequest);

        // Then
        assertEquals(expectedPage, result);
        verify(userRepository, times(1)).findAll(pageRequest);
        verify(userMapper, times(1)).toResponsePage(userPage);
    }

    @Test
    void getUserById() {
        // Given
        long userId = 1L;
        User user = new User();
        UserResponse response = new UserResponse();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        // When
        UserResponse result = userService.getById(userId);

        // Then
        assertEquals(response, result);
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    void getUserByIdThenUserNotFound() {
        // Given
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(UserNotFoundException.class, () -> userService.getById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void createUser() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setPassword("password");
        User user = new User();
        UserResponse response = new UserResponse();
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("password");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        // When
        UserResponse result = userService.create(userDto);

        // Then
        assertEquals(response, result);
        verify(userMapper, times(1)).toEntity(userDto);
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    void updateUser() {
        // Given
        UserDto userDto = new UserDto();
        Long id = 1L;
        User user = new User();
        UserResponse response = new UserResponse();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.updatePartial(user, userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        // When
        UserResponse result = userService.update(userDto, id);

        // Then
        assertEquals(response, result);
        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).updatePartial(user, userDto);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    void updateUserThenUserNotFound() {
        // Given
        UserDto userDto = new UserDto();
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(UserNotFoundException.class, () -> userService.update(userDto, id));
        verify(userRepository, times(1)).findById(id);
        verify(userMapper, never()).updatePartial(any(), any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toResponse(any());
    }

    @Test
    void deleteUser() {
        // Given
        long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.delete(userId);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }
}