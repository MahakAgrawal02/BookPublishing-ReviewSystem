package com.bookstore;

import java.util.List;
import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.bookstore.configs.Constants;
import com.bookstore.entity.Review;
import com.bookstore.services.ReviewService;
import com.bookstore.services.UserDetailsServiceImpl;

@SpringBootApplication
public class BookStoreApplication {

	public static void main(String[] args) {

		// Launches the Spring Boot application and returns the application context
		ConfigurableApplicationContext context = SpringApplication.run(BookStoreApplication.class, args);

		// Retrieves the custom user service implementation bean
		UserDetailsServiceImpl userService = context.getBean("userDetailsServiceImpl", UserDetailsServiceImpl.class);
		
		// Adds predefined users (Admin and Simple User) at startup
		userService.saveUser(Constants.ADMIN_USER);
		userService.saveUser(Constants.SIMPLE_USER);

		// Uncomment the lines below if you wish to pre-populate reviews for testing
		// ReviewService reviewService = context.getBean("reviewService", ReviewService.class);
		// initializeReview(reviewService);
	}
	
	// Initializes dummy reviews for testing/demo purposes
//	private static void initializeReview(ReviewService reviewService) {
//		Random random = new Random();
//		for (int i = 0; i < 10; i++) {
//			Review review = new Review();
//			review.setUsername(Constants.SIMPLE_USER.getUsername());
//			review.setComment("This is a great book.");
//			review.setBookId(random.nextInt(6)); // Assign random book ID between 0–5
//			reviewService.saveReview(review);
//		}
//
//		// Fetches and prints reviews for a specific book ID
//		List<Review> reviews = reviewService.getReviewsByBook_id(3);
//		for (Review review : reviews) {
//			System.err.println(review);
//		}
//	}
}
