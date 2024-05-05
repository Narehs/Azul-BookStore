package org.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.dto.AuthorDto;
import org.bookstore.response.AuthorResponse;
import org.bookstore.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/author")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthorController {


    private final AuthorService authorService;

    @Operation(
            summary = "Create new Author",
            description = "Create new Author providing Author information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful creation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AuthorResponse addAuthor(@Valid @RequestBody AuthorDto authorDto) {
        log.info("Received request to create author: {}", authorDto);
        return authorService.add(authorDto);
    }

    @Operation(
            summary = "Get Author",
            description = "Getting specific Author by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @GetMapping("/{id}")
    public AuthorResponse getAuthorById(@PathVariable Long id) {
        log.info("Received request to get author by ID: {}", id);
        return authorService.getById(id);
    }
    @Operation(
            summary = "Update Author",
            description = "Updating specific Author by Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AuthorResponse updateAuthor(@PathVariable Long id,@Valid @RequestBody AuthorDto authorDto) {
        log.info("Received request to update author with ID {}: {}", id, authorDto);
        return authorService.update(id, authorDto);
    }

    @Operation(
            summary = "Delete Author",
            description = "Deleting specific Author by Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful delete"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteAuthor(@PathVariable Long id) {
        log.info("Received request to delete author with ID: {}", id);
        authorService.delete(id);
    }

    @Operation(
            summary = "Getting Authors",
            description = "Getting All Authors in pagination format"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<AuthorResponse> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Received request to get all authors with pagination: page={}, size={}", page, size);
        return authorService.getAll(PageRequest.of(page, size));
    }
}

