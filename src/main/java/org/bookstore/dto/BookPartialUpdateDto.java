package org.bookstore.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BookPartialUpdateDto {
    private String title;

    @NonNull
    private String isbn;

    private String price;

    private Date writtenDate;

    private List<Long> genreIds = new ArrayList<>();

    private List<Long> authorIds = new ArrayList<>();
}
