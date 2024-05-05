package org.bookstore.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.dto.AuthorDto;
import org.bookstore.dto.BookDto;
import org.bookstore.dto.BookPartialUpdateDto;
import org.bookstore.dto.GenreDto;
import org.bookstore.exception.BookNotFoundException;
import org.bookstore.mapper.AuthorMapper;
import org.bookstore.mapper.BookMapper;
import org.bookstore.mapper.GenreMapper;
import org.bookstore.model.Author;
import org.bookstore.model.Book;
import org.bookstore.model.Genre;
import org.bookstore.repository.BookRepository;
import org.bookstore.response.AuthorResponse;
import org.bookstore.response.BookResponse;
import org.bookstore.response.GenreResponse;
import org.bookstore.service.AuthorService;
import org.bookstore.service.BookService;
import org.bookstore.service.GenreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;


    @Transactional
    public BookResponse add(BookDto bookDto) {

        log.debug("Adding book: {}", bookDto.getTitle());

        Book book = bookMapper.toEntity(bookDto);
        for (GenreDto genre : bookDto.getGenres()) {
            GenreResponse genreResponse = genreService.add(genreService.getByName(genre.getName())
                    .map(genreMapper::mapResponseToDto).orElse(genre));
            book.getGenres().add(genreMapper.mapResponseToEntity(genreResponse));
        }

        for (AuthorDto author : bookDto.getAuthors()) {
            AuthorResponse authorResponse = authorService.add(authorService.getAuthorByFirstNameAndIdentificationNumber(author.getFirstName(), author.getIdentificationNumber())
                            .map(authorMapper::mapResponseToDto)
                    .orElse(author));
            book.getAuthors().add(authorMapper.mapResponseToEntity(authorResponse));
        }

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse assignGenreToBook(Long bookId, Long genreId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(String.format("Book with id %d not found", bookId)));

        Genre genre = genreMapper.mapResponseToEntity(genreService.getById(genreId));

        book.getGenres().add(genre);

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse assignAuthorToBook(Long bookId, Long authorId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(String.format("Book with id %d not found", bookId)));

        Author author = authorMapper.mapResponseToEntity(authorService.getById(authorId));

        book.getAuthors().add(author);

        return bookMapper.toResponse(bookRepository.save(book));
    }

    public BookResponse getById(Long id) {
        log.debug("Fetching book by ID: {}", id);
        return bookMapper.toResponse(bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(String.format("Book with id %d not found", id))));
    }

    public BookResponse update(Long id, BookDto bookDto) {
        log.debug("Updating book with ID: {}", id);
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException(String.format("Book with id %d not found", id)));
        Book entity = bookMapper.mapToEntity(book, bookDto);
        Book updatedBook = bookRepository.save(entity);
        log.info("Book updated successfully: {}", updatedBook.getTitle());
        return bookMapper.toResponse(updatedBook);
    }

    @Override
    public BookResponse updatePartially(Long id, BookPartialUpdateDto bookDto) {
        log.debug("Updating book with ID: {}", id);
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException(String.format("Book with id %d not found", id)));
        Book partialMappedToEntity = bookMapper.mapPartialToEntity(bookDto, book);
        bookDto.getAuthorIds().forEach(aId->assignAuthorToBook(id, aId));
        bookDto.getGenreIds().forEach(gId->assignGenreToBook(id, gId));
        Book updatedBook = bookRepository.save(partialMappedToEntity);
        log.info("Book updated successfully: {}", updatedBook.getTitle());
        return bookMapper.toResponse(updatedBook);
    }

    public void delete(Long id) {
        log.debug("Deleting book with ID: {}", id);
        bookRepository.findById(id)
                .ifPresent(bookRepository::delete);
        log.info("Book deleted successfully");
    }

    public Page<BookResponse> getAll(PageRequest of) {
        log.debug("Fetching all books with pagination: {}", of);
        return bookMapper.toResponsePage(bookRepository.findAll(of));
    }

    public Page<BookResponse> search(String searchKey, PageRequest of) {
        log.debug("Searching books with key: {} and pagination: {}", searchKey, of);
        return bookMapper.toResponsePage(bookRepository.searchBooksUsingCriteria(searchKey, of));
    }
}
