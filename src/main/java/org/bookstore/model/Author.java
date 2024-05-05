package org.bookstore.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Author")
@Getter
@Setter
public class Author extends BaseEntity {

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private Long identificationNumber;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    private List<Book> books = new ArrayList<>();

}
