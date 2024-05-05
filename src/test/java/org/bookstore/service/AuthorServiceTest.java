package org.bookstore.service;

import org.bookstore.dto.AuthorDto;
import org.bookstore.exception.AuthorNotFoundException;
import org.bookstore.mapper.AuthorMapper;
import org.bookstore.mapper.BookMapper;
import org.bookstore.model.Author;
import org.bookstore.repository.AuthorRepository;
import org.bookstore.repository.BookRepository;
import org.bookstore.response.AuthorResponse;
import org.bookstore.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addAuthor() {
        // Given
        AuthorResponse response = new AuthorResponse();
        AuthorDto dto = new AuthorDto("Name","surname",1234L);
        Author author = new Author();
        when(authorMapper.toEntity(dto)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toResponse(author)).thenReturn(response);

        // When
        AuthorResponse result = authorService.add(dto);

        // Then
        assertEquals(response, result);
        verify(authorMapper, times(1)).toEntity(dto);
        verify(authorRepository, times(1)).save(author);
        verify(authorMapper, times(1)).toResponse(author);
    }

    @Test
    void getAuthorById() {
        // Given
        long authorId = 1L;
        AuthorResponse response = new AuthorResponse();
        Author author = new Author();
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorMapper.toResponse(author)).thenReturn(response);

        // When
        AuthorResponse result = authorService.getById(authorId);

        // Then
        assertEquals(response, result);
        verify(authorRepository, times(1)).findById(authorId);
        verify(authorMapper, times(1)).toResponse(author);
    }

    @Test
    void getAuthorByIdThenException() {
        // Given
        long authorId = 1L;
        //when
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());
        //Then
        assertThrows(AuthorNotFoundException.class, () -> authorService.getById(authorId));
        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    void updateAuthor() {
        // Given
        long authorId = 1L;
        AuthorResponse response = new AuthorResponse();
        AuthorDto dto = new AuthorDto("name","surname",1234L);
        Author author = new Author();
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorMapper.mapToEntity(author, dto)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toResponse(author)).thenReturn(response);

        // When
        AuthorResponse result = authorService.update(authorId, dto);

        // Then
        assertEquals(response, result);
        verify(authorRepository, times(1)).findById(authorId);
        verify(authorMapper, times(1)).mapToEntity(author, dto);
        verify(authorRepository, times(1)).save(author);
        verify(authorMapper, times(1)).toResponse(author);
    }

    @Test
    void testUpdateAuthorThenException() {
        // Arrange
        long authorId = 1L;
        AuthorDto authorDto = new AuthorDto("Name","surname",1234L);
        authorDto.setFirstName("John");
        authorDto.setLastName("Doe");

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> {
            authorService.update(authorId, authorDto);
        });

        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, never()).save(any());
    }

    @Test
    void deleteAuthor() {
        // Given
        long authorId = 1L;
        Author author = new Author();
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        // When
        authorService.delete(authorId);

        // Then
        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, times(1)).delete(author);
    }


    @Test
    void getAllAuthors() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Author> authorPage = Page.empty();
        when(authorRepository.findAll(pageRequest)).thenReturn(authorPage);
        Page<AuthorResponse> authorResponsePage = Page.empty();
        when(authorMapper.toResponsePage(authorPage)).thenReturn(authorResponsePage);

        // When
        Page<AuthorResponse> result = authorService.getAll(pageRequest);

        // Then
        assertEquals(authorResponsePage, result);
        verify(authorRepository, times(1)).findAll(pageRequest);
        verify(authorMapper, times(1)).toResponsePage(authorPage);
    }
}