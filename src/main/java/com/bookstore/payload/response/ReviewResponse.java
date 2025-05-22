package com.bookstore.payload.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private String reviewerUsername;
    private Integer rating;
    private String comment;
    private LocalDateTime timestamp;
} 