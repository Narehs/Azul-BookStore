package org.bookstore.service;

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
import org.bookstore.service.impl.AuthorServiceImpl;
import org.bookstore.service.impl.BookServiceImpl;
import org.bookstore.service.impl.GenreServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private GenreMapper genreMapper;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private GenreServiceImpl genreService;

    @Mock
    private AuthorServiceImpl authorService;

    private BookDto createBookDto() {
        BookDto bookDto = new BookDto("title","123r4L","123", null, Collections.emptyList(), Collections.emptyList());
        bookDto.setGenres(new ArrayList<>());
        bookDto.setAuthors(new ArrayList<>());

        GenreDto genreDto = new GenreDto("Sample Genre");
        bookDto.getGenres().add(genreDto);


        AuthorDto authorDto = new AuthorDto("Sample First Name","Salmasian",1234L);
        bookDto.getAuthors().add(authorDto);

        return bookDto;
    }

    private GenreDto createGenreDto() {
        GenreDto genreDto = new GenreDto("Fiction");
        return genreDto;
    }

    private AuthorDto createAuthorDto() {
        AuthorDto authorDto = new AuthorDto("Joe","Doe",1234L);
        return authorDto;
    }
    private Book createBookEntity(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());

        return book;
    }

    private GenreResponse createGenreResponse(GenreDto genreDto) {
        GenreResponse genreResponse = new GenreResponse();
        genreResponse.setId(1L);
        genreResponse.setName(genreDto.getName());
        return genreResponse;
    }

    private AuthorResponse createAuthorResponse(AuthorDto authorDto) {
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setId(1L);
        authorResponse.setFirstName(authorDto.getFirstName());
        authorResponse.setIdentificationNumber(authorDto.getIdentificationNumber());
        return authorResponse;
    }

    private Book createUpdatedBookEntity(BookDto bookDto) {
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Sample Book");
        return updatedBook;
    }

    private BookPartialUpdateDto createPartialUpdateDto() {
        BookPartialUpdateDto partialUpdateDto = new BookPartialUpdateDto("1234");
        partialUpdateDto.setAuthorIds(Arrays.asList(1L, 2L));
        partialUpdateDto.setGenreIds(Arrays.asList(1L, 2L));

        return partialUpdateDto;
    }

    @Test
    void addBook() {
        //Given
        BookDto bookDto = createBookDto();
        Book book = createBookEntity(bookDto);
        GenreDto genreDto = bookDto.getGenres().get(0);
        GenreResponse genreResponse = createGenreResponse(genreDto);
        AuthorDto authorDto = bookDto.getAuthors().get(0);
        AuthorResponse authorResponse = createAuthorResponse(authorDto);

        when(genreService.getByName(genreDto.getName())).thenReturn(Optional.of(genreResponse));
        when(genreMapper.mapResponseToDto(genreResponse)).thenReturn(genreDto);


        when(authorService.getAuthorByFirstNameAndIdentificationNumber(authorDto.getFirstName(), authorDto.getIdentificationNumber()))
                .thenReturn(Optional.of(authorResponse));
        when(authorMapper.mapResponseToDto(authorResponse)).thenReturn(authorDto);

        when(bookRepository.save(any(Book.class))).thenReturn(book);


        when(bookMapper.toEntity(bookDto)).thenReturn(book);
        when(bookMapper.toResponse(book)).thenReturn(new BookResponse());

        //When
        BookResponse response = bookService.add(bookDto);

        //Then
        verify(genreService, times(1)).getByName(genreDto.getName());
        verify(authorService, times(1)).getAuthorByFirstNameAndIdentificationNumber(authorDto.getFirstName(), authorDto.getIdentificationNumber());
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toEntity(bookDto);
        verify(bookMapper, times(1)).toResponse(book);
    }


    @Test
     void testAssignGenreToBook() {
        //Given
        Long bookId = 1L;
        Long genreId = 1L;
        Book book = new Book();
        Genre genre = new Genre();

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);


        when(genreService.getById(genreId)).thenReturn(createGenreResponse(createGenreDto()));


        when(genreMapper.mapResponseToEntity(any())).thenReturn(genre);


        when(bookMapper.toResponse(any())).thenReturn(new BookResponse());


        BookResponse response = bookService.assignGenreToBook(bookId, genreId);

        // Then
        verify(bookRepository, times(1)).findById(bookId);
        verify(genreService, times(1)).getById(genreId);
        verify(genreMapper, times(1)).mapResponseToEntity(any());
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toResponse(any());

        assertNotNull(response);
    }

    @Test
    void testAssignAuthorToBook() {
        // Given
        Long bookId = 1L;
        Long authorId = 1L;
        Book book = new Book();
        Author author = new Author();

        // When
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);


        when(authorService.getById(authorId)).thenReturn(createAuthorResponse(createAuthorDto()));


        when(authorMapper.mapResponseToEntity(any())).thenReturn(author);


        when(bookMapper.toResponse(any())).thenReturn(new BookResponse());

        //Given
        BookResponse response = bookService.assignAuthorToBook(bookId, authorId);

        //Then
        verify(bookRepository, times(1)).findById(bookId);
        verify(authorService, times(1)).getById(authorId);
        verify(authorMapper, times(1)).mapResponseToEntity(any());
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toResponse(any());

        assertNotNull(response);
    }

    @Test
    void getBookById() {
        // Given
        long bookId = 1L;
        Book book = new Book();
        BookResponse bookResponse = new BookResponse();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(bookResponse);

        // When
        BookResponse result = bookService.getById(bookId);

        // Then
        assertEquals(bookResponse, result);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toResponse(book);
    }

    @Test
    void getBookByIdThenBookNotFound() {
        // Given
        long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(BookNotFoundException.class, () -> bookService.getById(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testUpdate() {
        // Given
        Long bookId = 1L;
        BookDto bookDto = createBookDto();
        Book book = createBookEntity(bookDto);
        Book updatedBook = createUpdatedBookEntity(bookDto);
        BookResponse bookResponse = new BookResponse();
        bookResponse.setTitle("Updated Title");


        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);


        when(bookMapper.mapToEntity(any(Book.class), eq(bookDto))).thenReturn(updatedBook);
        when(bookMapper.toResponse(updatedBook)).thenReturn(bookResponse);

        // // When
        BookResponse response = bookService.update(bookId, bookDto);

        // Then
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(updatedBook);
        verify(bookMapper, times(1)).mapToEntity(book, bookDto);
        verify(bookMapper, times(1)).toResponse(updatedBook);
        assertEquals("Updated Title", response.getTitle());
    }

    @Test
    void testUpdatePartially() {
        // Given
        Long bookId = 1L;
        BookPartialUpdateDto partialUpdateDto = createPartialUpdateDto();
        Book book = createBookEntity(createBookDto());
        Book updatedBook = createUpdatedBookEntity(createBookDto());
        BookResponse bookResponse = new BookResponse();
        bookResponse.setTitle("Updated Title");
        // When
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);


        when(bookMapper.mapPartialToEntity(any(BookPartialUpdateDto.class), any(Book.class))).thenReturn(updatedBook);
        when(bookMapper.toResponse(updatedBook)).thenReturn(bookResponse);


        BookResponse response = bookService.updatePartially(bookId, partialUpdateDto);

        // Then
        verify(bookRepository, times(5)).findById(bookId);
        verify(bookRepository, times(5)).save(updatedBook);
        verify(bookMapper, times(1)).mapPartialToEntity(partialUpdateDto, book);
        verify(bookMapper, times(5)).toResponse(updatedBook);
        assertEquals("Updated Title", response.getTitle());
    }

    @Test
    void updateBookThenBookNotFound() {
        // Given
        long bookId = 1L;
        BookDto bookDto = new BookDto("title","123r4L","123", null, Collections.emptyList(), Collections.emptyList());
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(BookNotFoundException.class, () -> bookService.update(bookId, bookDto));
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, never()).mapPartialToEntity(any(), any());
        verify(bookRepository, never()).save(any());
        verify(bookMapper, never()).toResponse(any());
    }

    @Test
    void deleteBook() {
        // Given
        long bookId = 1L;
        Book book = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // When
        bookService.delete(bookId);

        // Then
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void getAllBooks() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Book> bookPage = Page.empty();
        when(bookRepository.findAll(pageRequest)).thenReturn(bookPage);
        Page<BookResponse> expectedPage = Page.empty();
        when(bookMapper.toResponsePage(bookPage)).thenReturn(expectedPage);

        // When
        Page<BookResponse> result = bookService.getAll(pageRequest);

        // Then
        assertEquals(expectedPage, result);
        verify(bookRepository, times(1)).findAll(pageRequest);
        verify(bookMapper, times(1)).toResponsePage(bookPage);
    }

    @Test
    void searchBooks() {
        // Given
        String searchKey = "Java";
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Book> bookPage = Page.empty();
        when(bookRepository.searchBooksUsingCriteria(searchKey, pageRequest)).thenReturn(bookPage);
        Page<BookResponse> expectedPage = Page.empty();
        when(bookMapper.toResponsePage(bookPage)).thenReturn(expectedPage);

        // When
        Page<BookResponse> result = bookService.search(searchKey, pageRequest);

        // Then
        assertEquals(expectedPage, result);
        verify(bookRepository, times(1)).searchBooksUsingCriteria(searchKey, pageRequest);
        verify(bookMapper, times(1)).toResponsePage(bookPage);
    }
}