package com.bookstore.payload.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    // Unique identifier for the book
    private Integer bookId;
    
    // Title of the book
    private String title;
    
    // Content or description of the book
    private String content;
    
    // Username of the author who published the book
    private String authorUsername;
    
    // List of reviews associated with the book
    private List<ReviewResponse> reviews;
} 
