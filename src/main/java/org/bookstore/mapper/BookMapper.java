package org.bookstore.mapper;

import org.bookstore.dto.BookDto;
import org.bookstore.dto.BookPartialUpdateDto;
import org.bookstore.model.Book;
import org.bookstore.response.BookResponse;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface BookMapper {


    BookResponse toResponse(Book entity);

    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "genres", ignore = true)
    Book toEntity(BookDto dto);

    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "genres", ignore = true)
    Book mapToEntity(@MappingTarget Book entity, BookDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book mapPartialToEntity(BookPartialUpdateDto dto, @MappingTarget Book entity);

    default Page<BookResponse> toResponsePage(Page<Book> bookPage) {
        return bookPage.map(this::toResponse);
    }
}

