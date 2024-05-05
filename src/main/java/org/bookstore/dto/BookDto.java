package org.bookstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    @NotNull
    private String title;

    @NotNull
    private String isbn;

    @NotNull
    private String price;

    private Date writtenDate;

    private List<GenreDto> genres;

    private List<AuthorDto> authors;
}
