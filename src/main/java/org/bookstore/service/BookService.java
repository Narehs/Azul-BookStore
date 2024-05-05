package org.bookstore.service;

import org.bookstore.dto.BookDto;
import org.bookstore.dto.BookPartialUpdateDto;
import org.bookstore.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * Interface for the {@link org.bookstore.service.impl.BookServiceImpl} .
 * Provides methods to perform CRUD operations on books.
 */
public interface BookService {

    /**
     * Adds a new book.
     *
     * @param bookDto The book data to be added.
     * @return The response containing the added book data.
     */
    BookResponse add(BookDto bookDto);

    /**
     * Assigns a genre to a book.
     *
     * @param bookId  The ID of the book to which the genre will be assigned.
     * @param genreId The ID of the genre to be assigned to the book.
     * @return A {@link BookResponse} representing the updated book after assigning the genre.
     */
    BookResponse assignGenreToBook(Long bookId, Long genreId);

    /**
     * Assigns an author to a book.
     *
     * @param bookId   The ID of the book to which the author will be assigned.
     * @param authorId The ID of the author to be assigned to the book.
     * @return A {@link BookResponse} representing the updated book after assigning the author.
     */
    BookResponse assignAuthorToBook(Long bookId, Long authorId);

    /**
     * Retrieves a book by ID.
     *
     * @param id The ID of the book to retrieve.
     * @return The response containing the retrieved book data.
     * @throws org.bookstore.exception.BookNotFoundException If the book with the given ID is not found.
     */
    BookResponse getById(Long id);

    /**
     * Updates an existing book.
     *
     * @param id       The ID of the book to update.
     * @param bookDto  The updated book data.
     * @return The response containing the updated book data.
     * @throws org.bookstore.exception.BookNotFoundException If the book with the given ID is not found.
     */
    BookResponse update(Long id, BookDto bookDto);

    /**
     * Updates an existing book.
     *
     * @param id       The ID of the book to update.
     * @param bookDto  The updated book data.
     * @return The response containing the updated book data.
     * @throws org.bookstore.exception.BookNotFoundException If the book with the given ID is not found.
     */
    BookResponse updatePartially(Long id, BookPartialUpdateDto bookDto);

    /**
     * Deletes a book by ID.
     *
     * @param id The ID of the book to delete.
     * @throws org.bookstore.exception.BookNotFoundException If the book with the given ID is not found.
     */
    void delete(Long id);

    /**
     * Retrieves all books with pagination.
     *
     * @param of The pagination information.
     * @return The page of book responses.
     */
    Page<BookResponse> getAll(PageRequest of);

    /**
     * Searches books based on a search key with pagination.
     *
     * @param searchKey The search key.
     * @param of        The pagination information.
     * @return The page of book responses matching the search key.
     */
    Page<BookResponse> search(String searchKey, PageRequest of);
}
