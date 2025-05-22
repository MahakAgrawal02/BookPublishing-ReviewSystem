package com.bookstore.configs;

import com.bookstore.payload.request.UserRequest;

public class Constants {
	
	public static final String SAVE_BOOK_URL = "/admin/book/save";
	public static final String LOGIN_URL = "/api/auth/login";
	public static final String SIGNUP_URL = "/api/auth/signup";
	public static final String GET_ALL_BOOKS_URL = "/authenticated/book/all-books";
	public static final String WRITE_REVIEW_URL = "/authenticated/review/write";
	
	public static final UserRequest ADMIN_USER = new UserRequest("Admin", "1234567", "ADMIN");
	public static final UserRequest SIMPLE_USER = new UserRequest("simpleuser", "1234567", "USER");

}
