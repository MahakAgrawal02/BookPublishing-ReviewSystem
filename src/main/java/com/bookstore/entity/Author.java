package com.bookstore.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an author who is also a user.
 * Extends UserEntity and holds a list of books published by the author.
 */
@Entity
@Getter
@Setter
public class Author extends UserEntity {

    /**
     * List of books published by this author.
     * Mapped by the "author" field in the Book entity.
     */
    @OneToMany(mappedBy = "author")
    private List<Book> publishedBooks = new ArrayList<>();
}
