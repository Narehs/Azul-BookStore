package org.bookstore.service;

import org.bookstore.dto.AuthorDto;
import org.bookstore.response.AuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * Interface of the {@link org.bookstore.service.impl.AuthorServiceImpl}.
 * Provides methods to perform CRUD operations on authors.
 */
public interface AuthorService {

    /**
     * Adds a new author.
     *
     * @param authorDto The author data to be added.
     * @return The response containing the added author data.
     */
    AuthorResponse add(AuthorDto authorDto);

    /**
     * Retrieves an author by their first name and identification number.
     *
     * @param firstName            The first name of the author.
     * @param identificationNumber The identification number of the author.
     * @return An {@link Optional} containing an {@link AuthorResponse} if an author
     * with the specified first name and identification number is found,
     * otherwise an empty {@link Optional}.
     */
    Optional<AuthorResponse> getAuthorByFirstNameAndIdentificationNumber(String firstName, Long identificationNumber);

    /**
     * Retrieves an author by ID.
     *
     * @param id The ID of the author to retrieve.
     * @return The response containing the retrieved author data.
     * @throws org.bookstore.exception.AuthorNotFoundException If the author with the given ID is not found.
     */
    AuthorResponse getById(Long id);

    /**
     * Updates an existing author.
     *
     * @param id        The ID of the author to update.
     * @param authorDto The updated author data.
     * @return The response containing the updated author data.
     * @throws org.bookstore.exception.AuthorNotFoundException If the author with the given ID is not found.
     */
    AuthorResponse update(Long id, AuthorDto authorDto);

    /**
     * Deletes an author by ID.
     *
     * @param id The ID of the author to delete.
     * @throws org.bookstore.exception.AuthorNotFoundException If the author with the given ID is not found.
     */
    void delete(Long id);

    /**
     * Retrieves all authors with pagination.
     *
     * @param of The pagination information.
     * @return The page of author responses.
     */
    Page<AuthorResponse> getAll(PageRequest of);

}
