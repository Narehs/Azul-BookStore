package org.bookstore.service;

import org.bookstore.dto.UserDto;
import org.bookstore.model.User;
import org.bookstore.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * Interface for the {@link org.bookstore.service.impl.UserServiceimpl}.
 * Provides methods to perform CRUD operations on users.
 */

public interface UserService {

    /**
     * Retrieves all users with pagination.
     *
     * @param of The pagination information.
     * @return The page of user responses.
     */
    Page<UserResponse> get(PageRequest of);

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The response containing the retrieved user data.
     * @throws org.bookstore.exception.UserNotFoundException If the user with the given ID is not found.
     */
    UserResponse getById(Long id);

    /**
     * Retrieves a user by username.
     *
     * @param username The username of the user to retrieve.
     * @return An optional containing the user if found, otherwise empty.
     */
    Optional<User> getByUsername(String username);


    /**
     * Creates a new user.
     *
     * @param dto The user data to be created.
     * @return The response containing the created user data.
     */
    UserResponse create(UserDto dto);

    /**
     * Updates an existing user.
     *
     * @param dto The updated user data.
     * @param id  The ID of the user to update.
     * @return The response containing the updated user data.
     * @throws org.bookstore.exception.UserNotFoundException If the user with the given ID is not found.
     */
    UserResponse update(UserDto dto, Long id);

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     * @throws org.bookstore.exception.UserNotFoundException If the user with the given ID is not found.
     */
    void delete(Long id);
}