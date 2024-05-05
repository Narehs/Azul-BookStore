package org.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.dto.UserDto;
import org.bookstore.response.UserResponse;
import org.bookstore.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Create new User",
            description = "Create new User providing User information, specifying the Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful creation"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Received request to create user: {}", userDto);
        UserResponse response = userService.create(userDto);
        log.info("Created user: {}", response);
        return response;
    }

    @Operation(
            summary = "Update User",
            description = "Updating specific User by Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponse updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Long id) {
        log.info("Received request to update user with ID {}: {}", id, userDto);
        UserResponse response = userService.update(userDto, id);
        log.info("Updated user: {}", response);
        return response;
    }

    @Operation(
            summary = "Delete User",
            description = "Deleting specific User by Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful delete"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user with ID: {}", id);
        userService.delete(id);
        log.info("Deleted user with ID: {}", id);
    }

    @Operation(
            summary = "Getting Users",
            description = "Getting All Users in pagination format"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<UserResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Received request to get all users with pagination: page={}, size={}", page, size);
        Page<UserResponse> response = userService.get(PageRequest.of(page, size));
        log.info("Returned {} users", response.getTotalElements());
        return response;
    }

    @Operation(
            summary = "Get User",
            description = "Getting specific User by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        log.info("Received request to get user by ID: {}", id);
        UserResponse response = userService.getById(id);
        log.info("Returned user: {}", response);
        return response;
    }
}

