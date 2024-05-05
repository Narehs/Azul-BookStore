package org.bookstore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class GenrePartialUpdateDto {

    @NonNull
    private Long id;

    @NonNull
    private String name;
}
