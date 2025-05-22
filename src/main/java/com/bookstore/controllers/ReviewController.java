package com.bookstore.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.entity.Review;
import com.bookstore.payload.request.ReviewRequest;
import com.bookstore.payload.response.ReviewResponse;
import com.bookstore.services.ReviewService;
import com.bookstore.services.UserDetailsImpl;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/authenticated/review")
public class ReviewController {
	@Autowired
	ReviewService reviewService;
	
	@PostMapping(path = "/write/{book_id}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> writeReview(@PathVariable ("book_id") int bookId,
			@Valid @RequestBody ReviewRequest reviewRequest){
		
		// Get the authenticated user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		// Create review request with all required fields
		ReviewRequest request = new ReviewRequest();
		request.setBookId(bookId);
		request.setReviewerUsername(userDetails.getUsername());
		request.setRating(reviewRequest.getRating());
		request.setComment(reviewRequest.getComment());
		
		reviewService.saveReview(request);
		
		return new ResponseEntity<>("Review created successfully", HttpStatus.CREATED);
	}
	
	@GetMapping("/get-reviews-of/{book_id}")
	public ResponseEntity<?> getReviewsOf(@PathVariable ("book_id") int bookId) {
		List<Review> reviews = reviewService.getReviewsOf(bookId);
		
		// Convert Review entities to ReviewResponse DTOs
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
