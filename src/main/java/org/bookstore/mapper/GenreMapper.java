package org.bookstore.mapper;

import org.bookstore.dto.GenreDto;
import org.bookstore.model.Genre;
import org.bookstore.response.GenreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreResponse toResponse(Genre entity);

    Genre toEntity(GenreDto dto);

    Genre mapToEntity(@MappingTarget Genre entity, GenreDto dto);

    GenreDto mapResponseToDto(GenreResponse response);

    GenreDto mapEntityToDto(Genre genre);

    Genre mapResponseToEntity(GenreResponse response);

    default Page<GenreResponse> toResponsePage(Page<Genre> genrePage) {
        return genrePage.map(this::toResponse);
    }
}

