package com.bookstore.exceptions;

public class ReviewOutOfBoundsException extends RuntimeException {
    public ReviewOutOfBoundsException(String message) {
        super(message);
    }
} 