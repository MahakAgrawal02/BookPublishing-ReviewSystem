package com.bookstore.exceptions;

/**
 * Custom exception to indicate invalid input data.
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}
