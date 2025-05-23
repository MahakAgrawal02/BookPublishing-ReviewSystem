package com.bookstore.payload.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    // Username of the user who wrote the review
    private String reviewerUsername;
    
    // Rating given in the review (e.g., 1 to 5)
    private Integer rating;
    
    // Review comment text
    private String comment;
    
    // Timestamp when the review was created
    private LocalDateTime timestamp;
}
