package com.bookstore.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Author extends UserEntity {
    @OneToMany(mappedBy = "author")
    private List<Book> publishedBooks = new ArrayList<>();
} 