package org.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bookstore.dto.GenreDto;
import org.bookstore.response.GenreResponse;
import org.bookstore.service.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class GenereControllerTest {

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        genreService = mock(GenreService.class);
        genreController = new GenreController(genreService);
    }

    @Test
    void testAddGenre() {
        GenreDto genreDto = new GenreDto("name");
        genreDto.setName("Fantasy");

        GenreResponse response = new GenreResponse();
        response.setId(1L);
        response.setName(genreDto.getName());

        when(genreService.add(any(GenreDto.class))).thenReturn(response);

        GenreResponse result = genreController.addGenre(genreDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Fantasy", result.getName());
    }

    @Test
    void testGetGenreById() {
        GenreResponse response = new GenreResponse();
        response.setId(1L);
        response.setName("Fantasy");

        when(genreService.getById(anyLong())).thenReturn(response);

        GenreResponse result = genreController.getGenreById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Fantasy", result.getName());
    }

    @Test
    void testUpdateGenre() {
        GenreDto genreDto = new GenreDto("name");
        genreDto.setName("Science Fiction");

        GenreResponse response = new GenreResponse();
        response.setId(1L);
        response.setName(genreDto.getName());

        when(genreService.update(anyLong(), any(GenreDto.class))).thenReturn(response);

        GenreResponse result = genreController.updateGenre(1L, genreDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Science Fiction", result.getName());
    }

    @Test
    void testDeleteGenre() {
        genreController.deleteGenre(1L);

        verify(genreService, times(1)).delete(1L);
    }

    @Test
    void testGetAllGenre() {
        GenreResponse response = new GenreResponse();
        response.setId(1L);
        response.setName("Fantasy");

        Page<GenreResponse> pageResponse = new PageImpl<>(Collections.singletonList(response));

        when(genreService.getAll(any(PageRequest.class))).thenReturn(pageResponse);

        Page<GenreResponse> result = genreController.getAllGenre(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("Fantasy", result.getContent().get(0).getName());
    }
}

