package org.bookstore.service;

import org.bookstore.dto.GenreDto;
import org.bookstore.exception.GenreNotFoundException;
import org.bookstore.mapper.GenreMapper;
import org.bookstore.model.Genre;
import org.bookstore.repository.GenreRepository;
import org.bookstore.response.GenreResponse;
import org.bookstore.service.impl.GenreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenreServiceTest {

    @Mock
    private GenreMapper genreMapper;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addGenre() {
        GenreDto genreDto = new GenreDto("name");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Fantasy");

        when(genreMapper.toEntity(genreDto)).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(genre);

        GenreResponse expectedResponse = new GenreResponse();
        expectedResponse.setId(1L);
        expectedResponse.setName("Fantasy");

        when(genreMapper.toResponse(genre)).thenReturn(expectedResponse);

        GenreResponse response = genreService.add(genreDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Fantasy", response.getName());

        verify(genreMapper, times(1)).toEntity(genreDto);
        verify(genreRepository, times(1)).save(genre);
        verify(genreMapper, times(1)).toResponse(genre);
    }

    @Test
    void getGenreById() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Fantasy");

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        GenreResponse expectedResponse = new GenreResponse();
        expectedResponse.setId(1L);
        expectedResponse.setName("Fantasy");

        when(genreMapper.toResponse(genre)).thenReturn(expectedResponse);

        GenreResponse response = genreService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Fantasy", response.getName());

        verify(genreRepository, times(1)).findById(1L);
        verify(genreMapper, times(1)).toResponse(genre);
    }

    @Test
    void getGenreByIdThenGenreNotFoundException() {
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreService.getById(1L));

        verify(genreRepository, times(1)).findById(1L);
        verify(genreMapper, never()).toResponse(any());
    }

    @Test
    void updateGenre() {
        GenreDto genreDto = new GenreDto("name");

        Genre existingGenre = new Genre();
        existingGenre.setId(1L);
        existingGenre.setName("Adventure");

        Genre updatedGenre = new Genre();
        updatedGenre.setId(1L);
        updatedGenre.setName("Fantasy");

        when(genreRepository.findById(1L)).thenReturn(Optional.of(existingGenre));
        when(genreMapper.mapToEntity(existingGenre, genreDto)).thenReturn(updatedGenre);
        when(genreRepository.save(updatedGenre)).thenReturn(updatedGenre);

        GenreResponse expectedResponse = new GenreResponse();
        expectedResponse.setId(1L);
        expectedResponse.setName("Fantasy");

        when(genreMapper.toResponse(updatedGenre)).thenReturn(expectedResponse);

        GenreResponse response = genreService.update(1L, genreDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Fantasy", response.getName());

        verify(genreRepository, times(1)).findById(1L);
        verify(genreMapper, times(1)).mapToEntity(existingGenre, genreDto);
        verify(genreRepository, times(1)).save(updatedGenre);
        verify(genreMapper, times(1)).toResponse(updatedGenre);
    }

    @Test
    void deleteGenre() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Fantasy");

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        genreService.delete(1L);

        verify(genreRepository, times(1)).findById(1L);
        verify(genreRepository, times(1)).delete(genre);
    }


    @Test
    void getAllGenres() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Genre> genrePage = Page.empty();
        when(genreRepository.findAll(pageRequest)).thenReturn(genrePage);
        Page<GenreResponse> expectedPage = Page.empty();
        when(genreMapper.toResponsePage(genrePage)).thenReturn(expectedPage);

        // When
        Page<GenreResponse> result = genreService.getAll(pageRequest);

        // Then
        assertEquals(expectedPage, result);
        verify(genreRepository, times(1)).findAll(pageRequest);
        verify(genreMapper, times(1)).toResponsePage(genrePage);
    }
}