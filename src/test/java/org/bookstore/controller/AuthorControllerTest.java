package org.bookstore.controller;

import org.bookstore.dto.AuthorDto;
import org.bookstore.response.AuthorResponse;
import org.bookstore.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @BeforeEach
    void setUp() {
        authorService = mock(AuthorService.class);
        authorController = new AuthorController(authorService);
    }

    @Test
    void addAuthor() {
        // Given
        AuthorResponse authorResponse = new AuthorResponse();
        AuthorDto authorDto = new AuthorDto("nn", "ss", 1234L);
        when(authorService.add(any(AuthorDto.class))).thenReturn(authorResponse);

        // When
        AuthorResponse result = authorController.addAuthor(authorDto);

        // Then
        assertEquals(authorResponse, result);
        verify(authorService, times(1)).add(authorDto);
    }

    @Test
    void getAuthorById() {
        // Given
        long authorId = 1L;
        AuthorResponse authorResponse = new AuthorResponse();
        when(authorService.getById(authorId)).thenReturn(authorResponse);

        // When
        AuthorResponse result = authorController.getAuthorById(authorId);

        // Then
        assertEquals(authorResponse, result);
        verify(authorService, times(1)).getById(authorId);
    }

    @Test
    void updateAuthor() {
        // Given
        long authorId = 1L;
        AuthorResponse response = new AuthorResponse();
        AuthorDto authorDto = new AuthorDto("nn", "ss", 1234L);
        when(authorService.update(authorId, authorDto)).thenReturn(response);

        // When
        AuthorResponse result = authorController.updateAuthor(authorId, authorDto);

        // Then
        assertEquals(response, result);
        verify(authorService, times(1)).update(authorId, authorDto);
    }

    @Test
    void deleteAuthor() {
        // Given
        long authorId = 1L;

        // When
        authorController.deleteAuthor(authorId);

        // Then
        verify(authorService, times(1)).delete(authorId);
    }

    @Test
    void getAllAuthors() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<AuthorResponse> authorPage = Page.empty();
        when(authorService.getAll(pageRequest)).thenReturn(authorPage);

        // When
        Page<AuthorResponse> result = authorController.getAllAuthors(0, 10);

        // Then
        assertEquals(authorPage, result);
        verify(authorService, times(1)).getAll(pageRequest);
    }
}
