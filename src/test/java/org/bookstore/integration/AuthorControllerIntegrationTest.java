package org.bookstore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bookstore.controller.AuthorController;
import org.bookstore.dto.AuthorDto;
import org.bookstore.repository.UserRepository;
import org.bookstore.response.AuthorResponse;
import org.bookstore.service.AuthorService;
import org.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorController.class)
@AutoConfigureMockMvc
class AuthorControllerIntegrationTest {

    @MockBean
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookService bookService;


    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddAuthor() throws Exception {
        AuthorDto authorDto = new AuthorDto("nare","salmasian",1234L);
        authorDto.setFirstName("John");
        authorDto.setLastName("Doe");

        AuthorResponse response = new AuthorResponse();
        response.setId(1L);
        response.setFirstName("John");
        response.setLastName("Doe");

        when(authorService.add(any(AuthorDto.class))).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/author")
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAuthorById() throws Exception {
        AuthorResponse response = new AuthorResponse();
        response.setId(1L);
        response.setFirstName("John");
        response.setLastName("Doe");

        when(authorService.getById(1L)).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/author/1")
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateAuthor() throws Exception {
        AuthorDto authorDto = new AuthorDto("Nare","Sal",1234L);
        authorDto.setFirstName("John");
        authorDto.setLastName("Doe");

        AuthorResponse response = new AuthorResponse();
        response.setId(1L);
        response.setFirstName("John");
        response.setLastName("Doe");

        when(authorService.update(eq(1L), any(AuthorDto.class))).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/author/1")
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/v1/author/1").with(csrf()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllAuthors() throws Exception {
        AuthorResponse response = new AuthorResponse();
        response.setId(1L);
        response.setFirstName("John");
        response.setLastName("Doe");

        when(authorService.getAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.singletonList(response)));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/author")
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].lastName").value("Doe"));
    }
}

