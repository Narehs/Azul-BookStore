package org.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.dto.AuthorDto;
import org.bookstore.exception.AuthorNotFoundException;
import org.bookstore.exception.EntityAlreadyExistsException;
import org.bookstore.mapper.AuthorMapper;
import org.bookstore.mapper.BookMapper;
import org.bookstore.model.Author;
import org.bookstore.repository.AuthorRepository;
import org.bookstore.response.AuthorResponse;
import org.bookstore.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final BookMapper bookMapper;

    public AuthorResponse add(AuthorDto authorDto) {
        log.debug("Adding author: {}", authorDto.getIdentificationNumber());
        Optional<Author> existingAuthor =
                authorRepository.findByFirstNameAndIdentificationNumber(authorDto.getFirstName(), authorDto.getIdentificationNumber());
        if (existingAuthor.isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Author with name %s and identifcication number %d already exists"
                    , authorDto.getFirstName(), authorDto.getIdentificationNumber()));
        } else {
            return authorMapper.toResponse(authorRepository.save(authorMapper.toEntity(authorDto)));
        }
    }

    public Optional<AuthorResponse> getAuthorByFirstNameAndIdentificationNumber(String firstName, Long identificationNumber) {
        return authorRepository.findByFirstNameAndIdentificationNumber(firstName, identificationNumber)
                .map(authorMapper::toResponse);
    }

    public AuthorResponse getById(Long id) {
        log.debug("Fetching author by ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(String.format("Author with id %d not found", id)));
        log.info("Author fetched successfully: {}", author.getIdentificationNumber());
        return authorMapper.toResponse(author);
    }

    public AuthorResponse update(Long id, AuthorDto authorDto) {
        log.debug("Updating author with ID: {}", id);
        Author author = authorRepository.findById(id).orElseThrow(() ->
                new AuthorNotFoundException(String.format("Author with id %d not found", id)));

        Author partialMappedToEntity = authorMapper.mapToEntity(author, authorDto);
        Author savedAuthor = authorRepository.save(partialMappedToEntity);
        log.info("Author updated successfully: {}", savedAuthor.getIdentificationNumber());
        return authorMapper.toResponse(savedAuthor);
    }

    public void delete(Long id) {
        log.debug("Deleting author with ID: {}", id);
        authorRepository.findById(id).ifPresent(a -> {
            a.getBooks().forEach(b -> b.getAuthors().remove(a));
            authorRepository.delete(a);
        });

        log.info("Author deleted successfully");
    }

    public Page<AuthorResponse> getAll(PageRequest of) {
        log.debug("Fetching all authors with pagination: {}", of);
        return authorMapper.toResponsePage(authorRepository.findAll(of));
    }
}
