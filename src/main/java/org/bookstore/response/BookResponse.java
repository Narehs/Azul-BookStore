package org.bookstore.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse extends BaseResponse{

    private String title;

    private String isbn;

    private String price;

    private Date writtenDate;

    private List<GenreResponse> genres = new ArrayList<>();

    private List<AuthorResponse> authors = new ArrayList<>();
}
