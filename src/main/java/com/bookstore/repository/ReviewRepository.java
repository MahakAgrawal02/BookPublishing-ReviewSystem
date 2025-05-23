package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // Find all reviews for a given book by bookId
    List<Review> findByBook_BookId(Integer bookId);
    
    // Find all reviews for a given book ordered by timestamp descending (most recent first)
    List<Review> findByBook_BookIdOrderByTimestampDesc(Integer bookId);
}
