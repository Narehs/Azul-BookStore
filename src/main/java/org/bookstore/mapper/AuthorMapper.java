package org.bookstore.mapper;

import org.bookstore.dto.AuthorDto;
import org.bookstore.model.Author;
import org.bookstore.response.AuthorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorResponse toResponse(Author entity);

    Author toEntity(AuthorDto dto);

    AuthorDto toDto(Author entity);
    AuthorDto mapResponseToDto(AuthorResponse response);

    Author mapToEntity(@MappingTarget Author entity, AuthorDto dto);

    Author mapResponseToEntity(AuthorResponse response);

    default Page<AuthorResponse> toResponsePage(Page<Author> authorPage) {
        return authorPage.map(this::toResponse);
    }
}

