package org.bookstore.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.dto.GenreDto;
import org.bookstore.exception.GenreNotFoundException;
import org.bookstore.mapper.GenreMapper;
import org.bookstore.model.Genre;
import org.bookstore.repository.GenreRepository;
import org.bookstore.response.GenreResponse;
import org.bookstore.service.GenreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreMapper genreMapper;

    private final GenreRepository genreRepository;

    @Override
    public GenreResponse add(GenreDto genreDto) {
        log.debug("Adding genre: {}", genreDto.getName());
        return genreRepository.findByName(genreDto.getName())
                .map(genreMapper::toResponse)
                .orElseGet(()->genreMapper.toResponse(genreRepository.save(genreMapper.toEntity(genreDto))));
    }

    @Override
    public GenreResponse getById(Long id) {
        log.debug("Fetching genre by ID: {}", id);
        return genreMapper.toResponse(genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(String.format("Genre with id %d not found", id))));
    }

    @Override
    public Optional<GenreResponse> getByName(String name) {
        log.debug("Fetching genre by Name: {}", name);
        return genreRepository.findByName(name).map(genreMapper::toResponse);
    }
    @Override
    public GenreResponse update(Long id, GenreDto genreDto) {
        log.debug("Updating genre with ID: {}", id);
        Genre genre = genreRepository.findById(id).orElseThrow(() ->
                new GenreNotFoundException(String.format("Author with id %d not found", id)));

        Genre partialMappedToEntity = genreMapper.mapToEntity(genre, genreDto);
        return genreMapper.toResponse(genreRepository.save(partialMappedToEntity));
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting genre with ID: {}", id);
        genreRepository.findById(id).ifPresent(a -> {
            a.getBooks().forEach(b -> b.getGenres().remove(a));
            genreRepository.delete(a);
        });
    }

    @Override
    public Page<GenreResponse> getAll(PageRequest of) {
        log.debug("Fetching all genres with pagination: {}", of);
        return genreMapper.toResponsePage(genreRepository.findAll(of));
    }
}
