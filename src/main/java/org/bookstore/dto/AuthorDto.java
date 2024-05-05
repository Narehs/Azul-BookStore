package org.bookstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    @NotNull
    private String firstName;

    private String lastName;

    @NotNull
    private Long identificationNumber;

}
