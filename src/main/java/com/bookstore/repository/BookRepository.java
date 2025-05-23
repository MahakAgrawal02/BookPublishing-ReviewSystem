package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.Book;
import com.bookstore.entity.UserEntity;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // Search books by title keyword (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String keyword);
    
    // Check if a book with the same title and author exists
    boolean existsByTitleAndAuthor(String title, UserEntity author);
    
    // Retrieve books ordered by average rating (highest first)
    @Query("SELECT b FROM Book b LEFT JOIN b.reviews r GROUP BY b ORDER BY COALESCE(AVG(r.rating), 0) DESC")
    List<Book> findTopBooksByAverageRating(int limit);
}
