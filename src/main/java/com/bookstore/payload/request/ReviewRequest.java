package com.bookstore.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
	
	// ID of the book being reviewed
	private Integer bookId;
	
	// Username of the reviewer
	private String reviewerUsername;
	
	// Rating given by the reviewer; mandatory and must be between 1 and 5
	@NotNull(message = "Rating is mandatory")
	@Min(value = 1, message = "Rating must be at least 1")
	@Max(value = 5, message = "Rating must be at most 5")
	private Integer rating;
	
	// Optional comment about the book
	private String comment;
}
