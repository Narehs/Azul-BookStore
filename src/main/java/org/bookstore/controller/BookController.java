package org.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.dto.BookDto;
import org.bookstore.dto.BookPartialUpdateDto;
import org.bookstore.response.BookResponse;
import org.bookstore.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookController {


    private final BookService bookService;

    @Operation(
            summary = "Add Books",
            description = "Adding book by specifying the Genre and Author"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public BookResponse addBook(@Valid @RequestBody BookDto bookDto) {
        log.info("Received request to add book: {}", bookDto);
        return bookService.add(bookDto);
    }

    @Operation(
            summary = "Get Book",
            description = "Getting specific Book by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN,'ROLE_USER')")
    public BookResponse getBookById(@PathVariable Long id) {
        log.info("Received request to get book by ID: {}", id);
        return bookService.getById(id);
    }

    @Operation(
            summary = "Assign genre to book",
            description = "Assigning specific Genre to book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @PutMapping("/{bookId}/genre/{genreId}")
    @PreAuthorize("hasRole('ROLE_ADMIN)")
    public BookResponse assignGenreToBook(@PathVariable Long bookId, @PathVariable Long genreId) {
        log.info("Assigning genre by ID: {} to book by ID {} ", genreId, bookId);
        return bookService.assignGenreToBook(bookId, genreId);
    }

    @Operation(
            summary = "Assign author to book",
            description = "Assigning specific author to book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @PutMapping("/{bookId}/author/{authorId}")
    @PreAuthorize("hasRole('ROLE_ADMIN)")
    public BookResponse assignAuthorToBook(@PathVariable Long bookId, @PathVariable Long authorId) {
        log.info("Assigning author by ID: {} to book by ID {}", authorId, bookId);
        return bookService.assignAuthorToBook(bookId, authorId);
    }

    @Operation(
            summary = "Update Book with all fields",
            description = "Updating specific Book by Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookResponse updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        log.info("Received request to update book with ID {}: {}", id, bookDto);
        return bookService.update(id, bookDto);
    }

    @Operation(
            summary = "Update Book Partially",
            description = "Updating specific Book by Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookResponse updateBookPartially(@PathVariable Long id, @Valid @RequestBody BookPartialUpdateDto bookDto) {
        log.info("Received request to update book with ID {}: {}", id, bookDto);
        return bookService.updatePartially(id, bookDto);
    }

    @Operation(
            summary = "Delete Book",
            description = "Deleting specific Book by Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteBook(@PathVariable Long id) {
        log.info("Received request to delete book with ID: {}", id);
        bookService.delete(id);
    }

    @Operation(
            summary = "Getting Books",
            description = "Getting All Books in pagination format"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<BookResponse> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Received request to get all books with pagination: page={}, size={}", page, size);
        return bookService.getAll(PageRequest.of(page, size));
    }

    @Operation(
            summary = "Searching Books",
            description = "Searching All Books by title, genre, author's name, author's lastname, isbn"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN,'ROLE_USER')")
    public Page<BookResponse> searchBooks(
            @RequestParam String searchKey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Received request to search books with key: {}", searchKey);
        return bookService.search(searchKey, PageRequest.of(page, size));
    }
}

