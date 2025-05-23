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
import com.bookstore.helpers.Converter;
import com.bookstore.payload.request.UserRequest;
import com.bookstore.services.UserDetailsServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Avoid JUnit exception on @BeforeAll non-static
public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserDetailsServiceImpl userService;
	
	/**
	 * Setup test data before running tests.
	 * Deletes user "johnD" if exists, then saves admin and simple users from Constants.
	 */
	@BeforeAll
	public void delete_johnD_username() {
		String username = "johnD";
		userService.deleteUser(username);
		userService.saveUser(Constants.ADMIN_USER);
		userService.saveUser(Constants.SIMPLE_USER);
	}
	
	/**
	 * Test signup with valid admin user data.
	 * Expects HTTP 201 Created status.
	 */
	@Test
	public void signup_shouldRegisterAdmin_whenDataIsValid() throws Exception {
		UserRequest user = new UserRequest("johnD", "1234567", "ADMIN");
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(Constants.SIGNUP_URL)
				.content(Converter.convertToString(user))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.CREATED.value(), status);
	}
	
	/**
	 * Test signup with invalid username (too short).
	 * Expects HTTP 400 Bad Request and error message about invalid username.
	 */
	@Test
	public void signup_shouldReturnBadRequest_whenDataIsInvalid() throws Exception {
		UserRequest user = new UserRequest("amb", "1234567", "ADMIN");
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(Constants.SIGNUP_URL)
				.content(Converter.convertToString(user))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		String response = mvcResult.getResponse().getContentAsString();
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), status);
		assertTrue(response.contains("Invalid Username"));
	}
	
	/**
	 * Test signup when the user already exists.
	 * Expects HTTP 409 Conflict and error message about user existence.
	 */
	@Test
	public void signup_shouldReturnConflictError_whenUserExists() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(Constants.SIGNUP_URL)
				.content(Converter.convertToString(Constants.ADMIN_USER))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		String response = mvcResult.getResponse().getContentAsString();
		
		assertEquals(HttpStatus.CONFLICT.value(), status);
		assertTrue(response.contains("already exists. Please, log in."));
	}
	
	/**
	 * Test login with valid registered user.
	 * Expects HTTP 200 OK and a token in the response.
	 */
	@Test
	public void login_shouldReturnToken_whenUserIsRegistered() throws Exception {
		UserRequest user = new UserRequest("ambodji", "1234567");
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(Constants.LOGIN_URL)
				.content(Converter.convertToString(user))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		String response = mvcResult.getResponse().getContentAsString();
		
		assertEquals(HttpStatus.OK.value(), status);
		assertTrue(response.contains("token"));
	}
	
	/**
	 * Test login with incorrect password.
	 * Expects HTTP 500 Internal Server Error and bad credentials error message.
	 */
	@Test
	public void login_shouldReturnBadCredentials_whenUsernameOrPasswordIncorrect() throws Exception {
		UserRequest user = new UserRequest("ambodji", "forgotPassword23");
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(Constants.LOGIN_URL)
				.content(Converter.convertToString(user))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		String response = mvcResult.getResponse().getContentAsString();
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);
		assertTrue(response.contains("errors") && response.contains("Bad credentials"));
	}
}
