package com.bookstore.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reviewId;
	@ManyToOne
	@JoinColumn(name = "book_id", referencedColumnName = "bookId")
	private Book book;
	@ManyToOne
	@JoinColumn(name = "reviewer_id")
	private UserEntity reviewer;
	@NotNull(message = "Rating is mandatory")
	private Integer rating;
	@NotBlank(message = "Comment is mandatory")
	private String comment;
	private LocalDateTime timestamp = LocalDateTime.now();
}
