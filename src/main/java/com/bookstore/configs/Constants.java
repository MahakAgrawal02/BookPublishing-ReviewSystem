package com.bookstore.configs;

import com.bookstore.payload.request.UserRequest;

/**
 * This class holds application-wide constant values,
 * including API endpoint paths and predefined user accounts.
 */
public class Constants {

	// API Endpoints
	public static final String SAVE_BOOK_URL = "/admin/book/save";               // Endpoint to save a book (Admin only)
	public static final String LOGIN_URL = "/api/auth/login";                    // Endpoint for user login
	public static final String SIGNUP_URL = "/api/auth/signup";                  // Endpoint for user signup
	public static final String GET_ALL_BOOKS_URL = "/authenticated/book/all-books"; // Endpoint to retrieve all books (authenticated users)
	public static final String WRITE_REVIEW_URL = "/authenticated/review/write"; // Endpoint to submit a review (authenticated users)

	// Predefined users to initialize at startup
	public static final UserRequest ADMIN_USER = new UserRequest("Admin", "1234567", "ADMIN");
	public static final UserRequest SIMPLE_USER = new UserRequest("simpleuser", "1234567", "USER");

}
