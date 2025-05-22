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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	@Id
	@NotNull(message = "book_id is mandatory")
	private Integer bookId;
	@NotBlank(message = "Title of the book is mandatory")
	private String title;
	@NotBlank(message = "Content of the book is mandatory")
	private String content;
	@ManyToOne
	@JoinColumn(name = "author_id")
	@NotNull(message = "Author is mandatory")
	private UserEntity author;
	@OneToMany(mappedBy = "book")
	private List<Review> reviews = new ArrayList<>();
}
