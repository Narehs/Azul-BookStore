package org.bookstore.service;

import org.bookstore.dto.GenreDto;
import org.bookstore.response.GenreResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * Interface of the {@link org.bookstore.service.impl.GenreServiceImpl}.
 * Provides methods to perform CRUD operations on genres.
 */
public interface GenreService {


    /**
     * Adds a new genre.
     *
     * @param genreDto The genre data to be added.
     * @return The response containing the added genre data.
     */
    GenreResponse add(GenreDto genreDto);


    /**
     * Retrieves a genre by ID.
     *
     * @param id The ID of the genre to retrieve.
     * @return The response containing the retrieved genre data.
     * @throws org.bookstore.exception.GenreNotFoundException If the genre with the given ID is not found.
     */
    GenreResponse getById(Long id);


    /**
     * Retrieves a genre by Name.
     *
     * @param name The Name of the genre to retrieve.
     * @return The response containing the retrieved genre data.
     * @throws org.bookstore.exception.GenreNotFoundException If the genre with the given Name is not found.
     */
    Optional<GenreResponse> getByName(String name);

    /**
     * Updates an existing genre.
     *
     * @param id       The ID of the genre to update.
     * @param genreDto The updated genre data.
     * @return The response containing the updated genre data.
     * @throws org.bookstore.exception.GenreNotFoundException If the genre with the given ID is not found.
     */
    GenreResponse update(Long id, GenreDto genreDto);

    /**
     * Deletes a genre by ID.
     *
     * @param id The ID of the genre to delete.
     * @throws org.bookstore.exception.GenreNotFoundException If the genre with the given ID is not found.
     */
    void delete(Long id);

    /**
     * Retrieves all genres with pagination.
     *
     * @param of The pagination information.
     * @return The page of genre responses.
     */
    Page<GenreResponse> getAll(PageRequest of);

}
