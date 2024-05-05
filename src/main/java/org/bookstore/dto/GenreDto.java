package org.bookstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {

    @NotNull
    private String name;
}
