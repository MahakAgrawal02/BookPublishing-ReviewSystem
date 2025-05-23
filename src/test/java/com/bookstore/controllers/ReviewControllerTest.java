package com.bookstore.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bookstore.configs.Constants;
import com.bookstore.entity.Review;
import com.bookstore.helpers.Converter;
import com.bookstore.payload.request.ReviewRequest;
import com.bookstore.payload.request.UserRequest;
import com.bookstore.services.BookService;
import com.bookstore.services.ReviewService;
import com.bookstore.repository.UserRepository;
import com.bookstore.utils.JwtUtil;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewControllerTest {
	
	private String jwtAdmin;
	private String jwtSimpleUser;
	
	// Base URLs for review-related endpoints
	private String urlWriteReview = "/authenticated/review/write/";
	private String urlGetReviewByBookId = "/authenticated/review/get-reviews-of/";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BookService bookService;
	
	@BeforeAll
	public void setUp() throws Exception {
		// Initialize JWT tokens for admin and simple user for authentication in tests
		jwtAdmin = getJWTafterMock(Constants.ADMIN_USER, Constants.LOGIN_URL);
		jwtSimpleUser = getJWTafterMock(Constants.SIMPLE_USER, Constants.LOGIN_URL);
	}
	
	@Test
	public void writeReview_ShouldWriteReview_WhenUserIsAdmin() throws Exception {
		// Use book ID 1 which should exist in the test database
		int bookId = 1;
		
		ReviewRequest reviewRequest = new ReviewRequest();
		reviewRequest.setRating(5);
		reviewRequest.setComment("This is an amazing book");
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(urlWriteReview + bookId)
				.content(Converter.convertToString(reviewRequest))
				.header("Authorization", "Bearer " + jwtAdmin)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
		assertTrue(mvcResult.getResponse().getContentAsString().contains("Review created successfully"));
	}
	
	@Test
	public void writeReview_ShouldWriteReview_WhenUserIsSimpleUser() throws Exception {
		// Use book ID 2 which should exist in the test database
		int bookId = 2;
		
		ReviewRequest reviewRequest = new ReviewRequest();
		reviewRequest.setRating(4);
		reviewRequest.setComment("Good book");
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(urlWriteReview + bookId)
				.content(Converter.convertToString(reviewRequest))
				.header("Authorization", "Bearer " + jwtSimpleUser)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
		assertTrue(mvcResult.getResponse().getContentAsString().contains("Review created successfully"));
	}
	
	@Test
	public void writeReview_ShouldReturnUnauthorized_WhenNoJwtProvided() throws Exception {
		int bookId = 1;
		ReviewRequest reviewRequest = new ReviewRequest();
		reviewRequest.setRating(5);
		reviewRequest.setComment("This is an amazing book");
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(urlWriteReview + bookId)
				.content(Converter.convertToString(reviewRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
		assertTrue(mvcResult.getResponse().getContentAsString().contains("error"));
	}
	
	@Test
	public void writeReview_ShouldReturnBadRequest_WhenReviewRequestIsInvalid() throws Exception {
		int bookId = 1;
		ReviewRequest reviewRequest = new ReviewRequest();
		// Missing required fields to test validation
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(urlWriteReview + bookId)
				.content(Converter.convertToString(reviewRequest))
				.header("Authorization", "Bearer " + jwtSimpleUser)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
		assertTrue(mvcResult.getResponse().getContentAsString().contains("errors"));
	}
	
	@Test
	public void getReviewOf_ReturnReviewsOfBookId_whenUserIsAdmin() throws Exception {
		int bookId = 1;
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(urlGetReviewByBookId + bookId)
				.header("Authorization", "Bearer " + jwtAdmin)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
		assertTrue(isReviewObjectReturned(mvcResult.getResponse().getContentAsString()));
	}
	
	@Test
	public void getReviewOf_ReturnReviewsOfBookId_whenUserIsSimpleUser() throws Exception {
		int bookId = 2;
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(urlGetReviewByBookId + bookId)
				.header("Authorization", "Bearer " + jwtSimpleUser)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
		assertTrue(isReviewObjectReturned(mvcResult.getResponse().getContentAsString()));
	}
	
	@Test
	public void getReviewOf_ReturnUnauthorized_whenNoJwtProvided() throws Exception {
		int bookId = 1;
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(urlGetReviewByBookId + bookId)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
		assertTrue(mvcResult.getResponse().getContentAsString().contains("error"));
	}
	
	private boolean isReviewObjectReturned(String responseBodyString) {
		return responseBodyString.contains("reviewerUsername") 
			&& responseBodyString.contains("rating") 
			&& responseBodyString.contains("comment");
	}

	private String getJWTafterMock(UserRequest user, String url) throws Exception {
		MvcResult mvcResult = GetMVCResultFromMockMvcPerform(user, url);
		String bodyString = mvcResult.getResponse().getContentAsString();
		
		// Verify we got a successful response
		if (mvcResult.getResponse().getStatus() != HttpStatus.OK.value()) {
			throw new RuntimeException("Login failed with status: " + mvcResult.getResponse().getStatus() + 
									 "response: " + bodyString);
		}
		
		return getTokenFromResponseAsString(bodyString);
	}

	private MvcResult GetMVCResultFromMockMvcPerform(UserRequest user, String url) throws Exception {
		return this.mockMvc.perform(MockMvcRequestBuilders.post(url)
			.content(Converter.convertToString(user))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andReturn();
	}

	private String getTokenFromResponseAsString(String responseAsString) {
		try {
			// Remove any whitespace and quotes
			String cleanResponse = responseAsString.trim();
			if (cleanResponse.startsWith("\"")) {
				cleanResponse = cleanResponse.substring(1);
			}
			if (cleanResponse.endsWith("\"")) {
				cleanResponse = cleanResponse.substring(0, cleanResponse.length() - 1);
			}
			return cleanResponse;
		} catch (Exception e) {
			throw new RuntimeException("Failed to extract JWT token from response: " + responseAsString, e);
		}
	}
}
