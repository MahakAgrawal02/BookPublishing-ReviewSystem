package com.bookstore.payload.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Integer bookId;
    private String title;
    private String content;
    private String authorUsername;
    private List<ReviewResponse> reviews;
} 