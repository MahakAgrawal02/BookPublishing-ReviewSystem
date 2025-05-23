package com.bookstore.exceptions;

/**
 * Exception thrown when a review rating is out of acceptable bounds.
 */
public class ReviewOutOfBoundsException extends RuntimeException {
    public ReviewOutOfBoundsException(String message) {
        super(message);
    }
}
