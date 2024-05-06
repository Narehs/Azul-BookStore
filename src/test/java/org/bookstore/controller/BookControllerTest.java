package org.bookstore.controller;

import org.bookstore.dto.BookDto;
import org.bookstore.dto.BookPartialUpdateDto;
import org.bookstore.response.BookResponse;
import org.bookstore.service.AuthorService;
import org.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BookControllerTest {

    private BookController bookController;
    private BookService bookService;
    private AuthorService authorService;


    @BeforeEach
    void setUp() {
        bookService = mock(BookService.class);
        bookController = new BookController(bookService);
    }

    @Test
    void addBook() {
        // Given
        BookDto bookDto = new BookDto("title", "123r4L", "123", null, Collections.emptyList(), Collections.emptyList());
        BookResponse response = new BookResponse();
        when(bookService.add(bookDto)).thenReturn(response);

        // When
        BookResponse addedBook = bookController.addBook(bookDto);

        // Then
        assertEquals(response, addedBook);
        verify(bookService, times(1)).add(bookDto);
    }

    @Test
    void testAssignGenreToBook() {

        Long bookId = 1L;
        Long genreId = 1L;
        BookResponse expectedResponse = new BookResponse();


        when(bookService.assignGenreToBook(bookId, genreId)).thenReturn(expectedResponse);

        BookResponse actualResponse = bookController.assignGenreToBook(bookId, genreId);


        verify(bookService, times(1)).assignGenreToBook(bookId, genreId);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getBookById() {
        // Given
        Long bookId = 123L;
        BookResponse response = new BookResponse();
        when(bookService.getById(bookId)).thenReturn(response);

        // When
        BookResponse actualBook = bookController.getBookById(bookId);

        // Then
        assertEquals(response, actualBook);
        verify(bookService, times(1)).getById(bookId);
    }

    @Test
    void updateBook() {
        // Given
        Long bookId = 123L;
        BookDto bookDto = new BookDto("title", "123r4L", "123", null, Collections.emptyList(), Collections.emptyList());
        BookResponse response = new BookResponse();
        when(bookService.update(bookId, bookDto)).thenReturn(response);

        // When
        BookResponse updatedBook = bookController.updateBook(bookId, bookDto);

        // Then
        assertEquals(response, updatedBook);
        verify(bookService, times(1)).update(bookId, bookDto);
    }

    @Test
    void testUpdateBookPartially() {
        // Given
        Long bookId = 1L;
        BookPartialUpdateDto bookDto = new BookPartialUpdateDto("1234");

        BookResponse updatedBookResponse = new BookResponse();
        when(bookService.updatePartially(bookId, bookDto))
                .thenReturn(updatedBookResponse);

        // When
        BookResponse response = bookController.updateBookPartially(bookId, bookDto);

        //Then
        verify(bookService).updatePartially(bookId, bookDto);
        assertNotNull(response);
    }

    @Test
    void deleteBook() {
        // Given
        Long bookId = 123L;

        // When
        bookController.deleteBook(bookId);

        // Then
        verify(bookService, times(1)).delete(bookId);
    }

    @Test
    void getAllBooks() {
        // Given
        Page<BookResponse> expectedPage = new PageImpl<>(Collections.emptyList());
        int page = 0;
        int size = 10;
        when(bookService.getAll(PageRequest.of(page, size))).thenReturn(expectedPage);

        // When
        Page<BookResponse> actualPage = bookController.getAllBooks(page, size);

        // Then
        assertEquals(expectedPage, actualPage);
        verify(bookService, times(1)).getAll(PageRequest.of(page, size));
    }

    @Test
    void searchBooks() {
        // Given
        String searchKey = "Java";
        Page<BookResponse> expectedPage = new PageImpl<>(Collections.emptyList());
        int page = 0;
        int size = 10;
        when(bookService.search(searchKey, PageRequest.of(page, size))).thenReturn(expectedPage);

        // When
        Page<BookResponse> actualPage = bookController.searchBooks(searchKey, page, size);

        // Then
        assertEquals(expectedPage, actualPage);
        verify(bookService, times(1)).search(searchKey, PageRequest.of(page, size));
    }
}
