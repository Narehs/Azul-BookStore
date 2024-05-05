package org.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.dto.GenreDto;
import org.bookstore.response.GenreResponse;
import org.bookstore.service.GenreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/genre")
@RequiredArgsConstructor
@Slf4j
@Validated
public class GenreController {


    private final GenreService genreService;

    @Operation(
            summary = "Create new Genre",
            description = "Create new Genre providing Genre information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful creation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GenreResponse addGenre(@Valid @RequestBody GenreDto genreDto) {
        log.info("Received request to create genre: {}", genreDto);
        GenreResponse genreResponse = genreService.add(genreDto);
        log.info("Created genre: {}", genreResponse);
        return genreResponse;
    }

    @Operation(
            summary = "Get Genre",
            description = "Getting specific Genre by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @GetMapping("/{id}")
    public GenreResponse getGenreById(@PathVariable Long id) {
        log.info("Received request to get genre by ID: {}", id);
        GenreResponse response = genreService.getById(id);
        log.info("Returned genre: {}", response);
        return response;
    }

    @Operation(
            summary = "Update Genre",
            description = "Updating specific Genre by Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GenreResponse updateGenre(@PathVariable Long id, @Valid @RequestBody GenreDto genreDto) {
        log.info("Received request to update genre with ID {}: {}", id, genreDto);
        GenreResponse genreResponse = genreService.update(id, genreDto);
        log.info("Updated genre: {}", genreResponse);
        return genreResponse;
    }

    @Operation(
            summary = "Delete Genre",
            description = "Deleting specific Genre by Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful delete"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteGenre(@PathVariable Long id) {
        log.info("Received request to delete genre with ID: {}", id);
        genreService.delete(id);
        log.info("Deleted genre with ID: {}", id);
    }

    @Operation(
            summary = "Getting Genre",
            description = "Getting All Genre in pagination format"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful fetch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<GenreResponse> getAllGenre(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Received request to get all genres with pagination: page={}, size={}", page, size);
        Page<GenreResponse> genres = genreService.getAll(PageRequest.of(page, size));
        log.info("Returned {} genres", genres.getTotalElements());
        return genres;
    }
}

