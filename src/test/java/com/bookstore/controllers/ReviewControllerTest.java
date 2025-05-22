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
	
	private String urlWriteReview = "/authenticated/review/write/";
	private String urlGetReviewByBookId = "/authenticated/review/get-reviews-of/";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ReviewService reviewService;

	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	BookService bookService;
	
	@BeforeAll
	public void setUp() throws Exception {
		
		jwtAdmin = getJWTafterMock(Constants.ADMIN_USER, Constants.LOGIN_URL);
		jwtSimpleUser = getJWTafterMock(Constants.SIMPLE_USER, Constants.LOGIN_URL);
		
	}
	
	@Test
	public void writeReview_ShouldWriteReview_WhenUserIsAdmin() throws Exception {
		String username = jwtUtil.getUserNameFromJwtToken(jwtAdmin);
		int book_id = 100;
		ReviewRequest reviewRequest = new ReviewRequest();
		reviewRequest.setComment("This is an amazing book");
		reviewRequest.setRating(5);
		
		Review review = new Review();
		review.setReviewer(userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
		review.setBook(bookService.getBookByID(book_id));
		review.setComment(reviewRequest.getComment());
		review.setRating(reviewRequest.getRating());
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(urlWriteReview+book_id)
				.content(Converter.convertToString(reviewRequest))
				.header("Authorization", "Bearer " + jwtAdmin)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		String bodyString = mvcResult.getResponse().getContentAsString();
		
		int actualStatus = mvcResult.getResponse().getStatus();
		int expectedStatus = HttpStatus.CREATED.value();
		
		assertEquals(expectedStatus, actualStatus);
		assertTrue(isReviewObjectReturned(bodyString));
		
	}
	
	@Test
	public void writeReview_ShouldWriteReview_WhenUserIsSimpleUser() throws Exception {
		String username = jwtUtil.getUserNameFromJwtToken(jwtAdmin);
		int book_id = 100;
		ReviewRequest reviewRequest = new ReviewRequest();
		reviewRequest.setComment("This is an amazing book");
		reviewRequest.setRating(5);
		
		Review review = new Review();
		review.setReviewer(userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
		review.setBook(bookService.getBookByID(book_id));
		review.setComment(reviewRequest.getComment());
		review.setRating(reviewRequest.getRating());
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(urlWriteReview+book_id)
				.content(Converter.convertToString(reviewRequest))
				.header("Authorization", "Bearer " + jwtSimpleUser)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		String bodyString = mvcResult.getResponse().getContentAsString();
		
		int actualStatus = mvcResult.getResponse().getStatus();
		int expectedStatus = HttpStatus.CREATED.value();
		
		assertEquals(expectedStatus, actualStatus);
		assertTrue(isReviewObjectReturned(bodyString));
		
	}
	
	@Test
	public void writeReview_ShouldReturnUnauthorized_WhenNoJwtProvided() throws Exception {
		String username = jwtUtil.getUserNameFromJwtToken(jwtAdmin);
		int book_id = 100;
		ReviewRequest reviewRequest = new ReviewRequest();
		reviewRequest.setComment("This is an amazing book");
		reviewRequest.setRating(5);
		
		Review review = new Review();
		review.setReviewer(userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
		review.setBook(bookService.getBookByID(book_id));
		review.setComment(reviewRequest.getComment());
		review.setRating(reviewRequest.getRating());
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(urlWriteReview+book_id)
				.content(Converter.convertToString(reviewRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		String bodyString = mvcResult.getResponse().getContentAsString();
		
		int actualStatus = mvcResult.getResponse().getStatus();
		int expectedStatus = HttpStatus.UNAUTHORIZED.value();
		
		assertEquals(expectedStatus, actualStatus);
		assertTrue(bodyString.contains("error")
				&& bodyString.contains("Full authentication is required to access this resource"));
		
	}
	
	@Test
	public void writeReview_ShouldReturnBadRequest_WhenReviewRequestIsNull() throws Exception {
		String username = jwtUtil.getUserNameFromJwtToken(jwtAdmin);
		int book_id = 100;
		ReviewRequest reviewRequest = new ReviewRequest();
		reviewRequest.setComment("This is an amazing book");
		reviewRequest.setRating(5);
		
		Review review = new Review();
		review.setReviewer(userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
		review.setBook(bookService.getBookByID(book_id));
		review.setComment(reviewRequest.getComment());
		review.setRating(reviewRequest.getRating());
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(urlWriteReview+book_id)
				.content(Converter.convertToString(reviewRequest))
				.header("Authorization", "Bearer " + jwtSimpleUser)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		String bodyString = mvcResult.getResponse().getContentAsString();
		
		int actualStatus = mvcResult.getResponse().getStatus();
		int expectedStatus = HttpStatus.BAD_REQUEST.value();
		
		assertEquals(expectedStatus, actualStatus);
		assertTrue(bodyString.contains("errors")
				&& bodyString.contains("comment is mandantory"));
		
	}
	
	@Test
	public void getReviewOf_ReturnReviewsOfBookId_whenUserIsAdmin() throws Exception {
		
		int book_id = 3;
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(urlGetReviewByBookId+book_id)
				.header("Authorization", "Bearer " + jwtAdmin)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		String bodyString = mvcResult.getResponse().getContentAsString();
		
		int actualStatus = mvcResult.getResponse().getStatus();
		int expectedStatus = HttpStatus.OK.value();
		
		assertEquals(expectedStatus, actualStatus);
		assertTrue(isReviewObjectReturned(bodyString));
		
	}
	
	@Test
	public void getReviewOf_ReturnReviewsOfBookId_whenUserIsSimpleUser() throws Exception {
		
		int book_id = 4;
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(urlGetReviewByBookId+book_id)
				.header("Authorization", "Bearer " + jwtSimpleUser)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		String bodyString = mvcResult.getResponse().getContentAsString();
		
		int actualStatus = mvcResult.getResponse().getStatus();
		int expectedStatus = HttpStatus.OK.value();
		
		assertEquals(expectedStatus, actualStatus);
		assertTrue(isReviewObjectReturned(bodyString));
	}
	
	@Test
	public void getReviewOf_ReturnUnauthorized_whenNoJwtProvided() throws Exception {
		
		int book_id = 4;
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(urlGetReviewByBookId+book_id)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		String bodyString = mvcResult.getResponse().getContentAsString();
		
		int actualStatus = mvcResult.getResponse().getStatus();
		int expectedStatus = HttpStatus.UNAUTHORIZED.value();
		
		assertEquals(expectedStatus, actualStatus);
		assertTrue(bodyString.contains("error")
				&& bodyString.contains("Full authentication is required to access this resource"));
	}
	
	private boolean isReviewObjectReturned(String responseBodyString) {
		return responseBodyString.contains("review_id")
			&& responseBodyString.contains("username")
			&& responseBodyString.contains("bookId")
			&& responseBodyString.contains("comment");
	}

	private String getJWTafterMock(UserRequest user, String url) throws Exception {
		
		MvcResult mvcResult = GetMVCResultFromMockMvcPerform(user, url);
		
		String bodyString = mvcResult.getResponse().getContentAsString();
		
		return getTokenFromResponseAsString(bodyString);
	}

	private MvcResult GetMVCResultFromMockMvcPerform(UserRequest user, String url) throws Exception {
		
		return this.mockMvc.perform(MockMvcRequestBuilders.post(url)
			.content(Converter.convertToString(user))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andReturn();
	}

	private String getTokenFromResponseAsString(String responseAsString) {
		
		return responseAsString.substring(10, responseAsString.length()-2);
	}

}
