package com.bookstore.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    @NotNull(message = "book_id is mandatory")
    private Integer bookId;
    
    @NotBlank(message = "Title of the book is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Title should not contain special characters")
    private String title;
    
    @NotBlank(message = "Content of the book is mandatory")
    private String content;
    
    private String authorUsername;
} 