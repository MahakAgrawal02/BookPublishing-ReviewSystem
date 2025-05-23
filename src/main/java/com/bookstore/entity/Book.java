package com.bookstore.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a Book in the system.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    /**
     * Unique identifier for the book.
     */
    @Id
    @NotNull(message = "book_id is mandatory")
    private Integer bookId;

    /**
     * Title of the book.
     */
    @NotBlank(message = "Title of the book is mandatory")
    private String title;

    /**
     * Content or description of the book.
     */
    @NotBlank(message = "Content of the book is mandatory")
    private String content;

    /**
     * The author of the book.
     * Maps to UserEntity, typically an Author.
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull(message = "Author is mandatory")
    private UserEntity author;

    /**
     * List of reviews associated with this book.
     * Mapped by the 'book' field in Review entity.
     */
    @OneToMany(mappedBy = "book")
    private List<Review> reviews = new ArrayList<>();
}
