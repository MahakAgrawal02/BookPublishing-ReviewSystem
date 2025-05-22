package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
	List<Review> findByBook_BookId(Integer bookId);
	
	List<Review> findByBook_BookIdOrderByTimestampDesc(Integer bookId);
}
