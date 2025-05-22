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
		
		//SpringApplication.run(BookReviewApplication.class, args);

		ConfigurableApplicationContext context = SpringApplication.run(BookStoreApplication.class, args);

		UserDetailsServiceImpl userService = context.getBean("userDetailsServiceImpl", UserDetailsServiceImpl.class);
		// ReviewService reviewService = context.getBean("reviewService", ReviewService.class);

		userService.saveUser(Constants.ADMIN_USER);
		userService.saveUser(Constants.SIMPLE_USER);
		
		// initializeReview(reviewService);
		


	}
	
//	private static void initializeReview(ReviewService reviewService) {
//		Random random = new Random();
//		for(int i=0; i<10;i++) {
//			
//			Review review = new Review();
//			review.setUsername(Constants.SIMPLE_USER.getUsername());
//			review.setComment("This is a great book.");
//			
//			review.setBookId(random.nextInt(6));
//			
//			reviewService.saveReview(review);
//		}
//		
//		List<Review> reviews = reviewService.getReviewsByBook_id(3);
//		for(Review review: reviews){
//			System.err.println(review);
//		}
//	}

}
