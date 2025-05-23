package com.bookstore.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.bookstore.entity.Review;
import com.bookstore.payload.request.ReviewRequest;
import com.bookstore.payload.response.ReviewResponse;
import com.bookstore.services.ReviewService;
import com.bookstore.services.UserDetailsImpl;

import jakarta.validation.Valid;

/**
 * REST controller for handling book review operations.
 */
@RestController
@RequestMapping("/authenticated/review")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	/**
	 * Allows an authenticated user to write a review for a specific book.
	 *
	 * @param bookId        the ID of the book to review
	 * @param reviewRequest the review details including rating and comment
	 * @return success message with CREATED status
	 */
	@PostMapping(path = "/write/{book_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> writeReview(@PathVariable("book_id") int bookId,
	                                     @Valid @RequestBody ReviewRequest reviewRequest) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		// Construct full review request with authenticated username and book ID
		ReviewRequest request = new ReviewRequest();
		request.setBookId(bookId);
		request.setReviewerUsername(userDetails.getUsername());
		request.setRating(reviewRequest.getRating());
		request.setComment(reviewRequest.getComment());

		reviewService.saveReview(request);

		return new ResponseEntity<>("Review created successfully", HttpStatus.CREATED);
	}

	/**
	 * Fetches all reviews associated with a specific book.
	 *
	 * @param bookId the ID of the book
	 * @return list of ReviewResponse DTOs
	 */
	@GetMapping("/get-reviews-of/{book_id}")
	public ResponseEntity<?> getReviewsOf(@PathVariable("book_id") int bookId) {
		List<Review> reviews = reviewService.getReviewsOf(bookId);

		List<ReviewResponse> reviewResponses = reviews.stream()
			.map(review -> new ReviewResponse(
				review.getReviewer().getUsername(),
				review.getRating(),
				review.getComment(),
				review.getTimestamp()
			))
			.collect(Collectors.toList());

		return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
	}
}
