package org.bookstore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bookstore.controller.BookController;
import org.bookstore.dto.AuthorDto;
import org.bookstore.dto.BookDto;
import org.bookstore.dto.BookPartialUpdateDto;
import org.bookstore.dto.GenreDto;
import org.bookstore.repository.UserRepository;
import org.bookstore.response.BookResponse;
import org.bookstore.service.AuthorService;
import org.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private BookService bookService;


    @MockBean
    private AuthorService authorService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void addBook() throws Exception {
        // Given
        BookDto bookDto = new BookDto("Sample Book", "123r4L", "123", null, Collections.emptyList(), Collections.emptyList());

        GenreDto genreDto = new GenreDto("Name");

        AuthorDto authorDto = new AuthorDto("Nare", "Salmasian", 1234L);
        bookDto.setGenres(List.of(genreDto));
        bookDto.setAuthors(List.of(authorDto));

        BookResponse bookResponse = new BookResponse();
        bookResponse.setTitle("Sample Book");

        Mockito.when(bookService.add(any(BookDto.class))).thenReturn(bookResponse);

        // When, Then
        mockMvc.perform(post("/api/v1/books")
                        .content(objectMapper.writeValueAsString(bookDto))
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Sample Book"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void getBookById() throws Exception {
        // Given
        long bookId = 1L;
        BookDto bookDto = new BookDto("title", "123r4L", "123", null, Collections.emptyList(), Collections.emptyList());
        GenreDto genreDto = new GenreDto("Name");
        genreDto.setName("BIOGRAPHY");
        bookDto.setTitle("Sample Book");
        bookDto.setGenres(List.of(genreDto));
        bookDto.setIsbn("1234");
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(bookId);
        bookResponse.setTitle("Sample Book");
        Mockito.when(bookService.getById(bookId)).thenReturn(bookResponse);

        // When, Then
        mockMvc.perform(get("/api/v1/books/{id}", bookId)
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Sample Book"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBook() throws Exception {
        // Given
        long bookId = 1L;
        BookDto bookDto = new BookDto("title", "123r4L", "123", null, Collections.emptyList(), Collections.emptyList());
        BookResponse bookResponse = new BookResponse();
        bookDto.setTitle("Updated Book");
        bookResponse.setId(bookId);
        bookResponse.setTitle("Updated Book");
        Mockito.when(bookService.update(eq(bookId), any(BookDto.class))).thenReturn(bookResponse);

        // When, Then
        mockMvc.perform(put("/api/v1/books/{id}", bookId)
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBook() throws Exception {
        // Given
        long bookId = 1L;

        // When, Then
        mockMvc.perform(delete("/api/v1/books/{id}", bookId)
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllBooks() throws Exception {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BookResponse> bookPage = Page.empty();
        Mockito.when(bookService.getAll(pageRequest)).thenReturn(bookPage);

        // When, Then
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void searchBooks() throws Exception {
        // Given
        String searchKey = "Sample";
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BookResponse> bookPage = Page.empty();
        Mockito.when(bookService.search(eq(searchKey), any(PageRequest.class))).thenReturn(bookPage);

        // When, Then
        mockMvc.perform(get("/api/v1/books/search")
                        .param("searchKey", searchKey))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateBookPartially() throws Exception {
        // Given
        Long bookId = 1L;
        BookPartialUpdateDto partialUpdateDto = new BookPartialUpdateDto("1234");
        partialUpdateDto.setAuthorIds(Arrays.asList(1L, 2L));
        partialUpdateDto.setGenreIds(Arrays.asList(1L, 2L));
        partialUpdateDto.setPrice("100");

        BookResponse bookResponse = new BookResponse();
        bookResponse.setPrice("100");
        Mockito.when(bookService.updatePartially(eq(bookId), any(BookPartialUpdateDto.class))).thenReturn(bookResponse);

        //When, Then
        mockMvc.perform(patch("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(partialUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value("100"));
    }
}

