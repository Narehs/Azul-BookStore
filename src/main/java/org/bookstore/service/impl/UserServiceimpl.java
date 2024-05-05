package org.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.dto.UserDto;
import org.bookstore.exception.UserNotFoundException;
import org.bookstore.mapper.UserMapper;
import org.bookstore.model.User;
import org.bookstore.repository.UserRepository;
import org.bookstore.response.UserResponse;
import org.bookstore.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceimpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public Page<UserResponse> get(PageRequest of) {
        log.debug("Fetching users with pagination: {}", of);
        return mapper.toResponsePage(repository.findAll(of));
    }

    public UserResponse getById(Long id) {
        log.debug("Fetching user by ID: {}", id);
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found")));
    }

    public Optional<User> getByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        return repository.findByUsernameAndIsEnabledTrue(username);
    }

    @Transactional
    public UserResponse create(UserDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        log.debug("Creating new user: {}", dto.getUsername());
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    public UserResponse update(UserDto dto, Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        log.debug("Updating user with ID {}: {}", id, dto.getUsername());
        return mapper.toResponse(repository.save(mapper.updatePartial(user, dto)));
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting user with ID: {}", id);
        repository.findById(id).ifPresent(repository::delete);
    }
}