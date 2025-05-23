package com.bookstore.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Review;
import com.bookstore.entity.UserEntity;
import com.bookstore.exceptions.ReviewOutOfBoundsException;
import com.bookstore.payload.request.ReviewRequest;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.ReviewRepository;
import com.bookstore.repository.UserRepository;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;
    
    @Autowired
    BookRepository bookRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    NotificationService notificationService;
    
    public void saveReview(ReviewRequest reviewRequest) {
        // Validate rating range
        if (reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
            throw new ReviewOutOfBoundsException("Rating must be between 1 and 5");
        }
        
        // Retrieve the book by ID or throw if not found
        Book book = bookRepository.findById(reviewRequest.getBookId())
            .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + reviewRequest.getBookId()));
        
        // Retrieve the reviewer by username or throw if not found
        UserEntity reviewer = userRepository.findByUsername(reviewRequest.getReviewerUsername())
            .orElseThrow(() -> new IllegalArgumentException("Reviewer not found with username: " + reviewRequest.getReviewerUsername()));
        
        // Create new Review entity and set details
        Review review = new Review();
        review.setBook(book);
        review.setReviewer(reviewer);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        
        // Save the review to the database
        reviewRepository.save(review);
        
        // Trigger asynchronous notification to the author
        notificationService.notifyAuthor(book.getAuthor().getEmail(), book.getTitle());
    }
    
    // Retrieve all reviews for a given book, ordered by most recent first
    public List<Review> getReviewsOf(int bookId) {
        return reviewRepository.findByBook_BookIdOrderByTimestampDesc(bookId);
    }
}
