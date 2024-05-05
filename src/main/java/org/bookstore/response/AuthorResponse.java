package org.bookstore.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthorResponse extends BaseResponse{

    private String firstName;

    private String lastName;

    private Long identificationNumber;

}
