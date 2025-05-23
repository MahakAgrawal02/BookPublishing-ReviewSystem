package com.bookstore.exceptions;

/**
 * Custom exception to indicate that a book already exists in the system.
 */
public class DuplicateBookException extends RuntimeException {
    public DuplicateBookException(String message) {
        super(message);
    }
}
