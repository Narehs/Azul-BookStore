package org.bookstore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bookstore.controller.GenreController;
import org.bookstore.dto.GenreDto;
import org.bookstore.repository.UserRepository;
import org.bookstore.response.GenreResponse;
import org.bookstore.service.AuthorService;
import org.bookstore.service.BookService;
import org.bookstore.service.GenreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GenreController.class)
@AutoConfigureMockMvc
class GenereControllerIntegrationTest {

    @MockBean
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookService bookService;


    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddGenre() throws Exception {
        GenreDto genreDto = new GenreDto("name");

        GenreResponse response = new GenreResponse();
        response.setId(1L);
        response.setName(genreDto.getName());

        when(genreService.add(any(GenreDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/genre")
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(genreDto.getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetGenreById() throws Exception {
        GenreResponse response = new GenreResponse();
        response.setId(1L);
        response.setName("Fantasy");

        when(genreService.getById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/genre/{id}", 1L).with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateGenre() throws Exception {
        GenreDto genreDto = new GenreDto("Science Fiction");

        GenreResponse response = new GenreResponse();
        response.setId(1L);
        response.setName(genreDto.getName());

        when(genreService.update(anyLong(), any(GenreDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/genre/{id}", 1L)
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Science Fiction"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteGenre() throws Exception {
        mockMvc.perform(delete("/api/v1/genre/{id}", 1L)
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllGenre() throws Exception {
        GenreResponse response = new GenreResponse();
        response.setId(1L);
        response.setName("Fantasy");

        Page<GenreResponse> pageResponse = new PageImpl<>(Collections.singletonList(response));

        when(genreService.getAll(any(PageRequest.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/genre").with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Fantasy"));
    }
}

