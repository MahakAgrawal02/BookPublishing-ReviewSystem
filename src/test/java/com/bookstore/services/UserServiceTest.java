package com.bookstore.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bookstore.configs.Constants;
import com.bookstore.payload.request.UserRequest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // avoid JUnitException
public class UserServiceTest {
	@Autowired
	private UserDetailsServiceImpl userService;

	@BeforeAll
	public void delele_rong_username() {
		//String username = "johnD";
		//UserRequest userToDelete = new UserRequest("usertodelete", "password1", "USER");
		
		//userService.deleteUser(username);
		
		
		// userService.saveUser(userToDelete);
		userService.saveUser(Constants.SIMPLE_USER);
		userService.saveUser(Constants.ADMIN_USER);
	}
	
	/*
	 * delete join-table has convertHibernateAccessException
	 */

//	@Test
//	public void saveUser_shouldTrue_whenNewUserIsValid() throws Exception {
//
//		UserRequest userInfo = new UserRequest();
//		userInfo.setUsername("johnD");
//		userInfo.setPassword("1234567");
//		userInfo.setRoleName("ADMIN");
//
//		assertTrue(userService.saveUser(userInfo));
//	}
	
	@Test 
	void saveUser_shouldReturnFalse_whenUsernameExists() {
		
		
		assertFalse(userService.saveUser(Constants.ADMIN_USER));
	}
	
	@Test
	void saveUser_shouldThrowException_whenUserIsNull() {
		UserRequest userInfo=null;
		
		Exception exception = assertThrows(NullPointerException.class, () -> {
			userService.saveUser(userInfo);
		});

		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains("is null"));
		
	}
	
	@Test
	void saveUser_shouldThrowException_whenUserFieldsAreNull() {
		UserRequest userInfo=new UserRequest();
		
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.saveUser(userInfo);
		});

		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains("cannot be null"));
	}
	
	@Test
	void loadUserByUsername_shouldReturnUser_whenUserExists() {
		String username = "ambodji";
		
		UserDetails user = userService.loadUserByUsername(username);
		
		assertNotNull(user);
	}
	
	@Test
	void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
		String username = "NonExistentUser";
		
		Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
			userService.loadUserByUsername(username);
		});
		
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains("is not found."));
		
	}
	
	@Test
	void deleteUser_shouldDeleteZeroUser_whenWhenUsernameDoesNotExist() {
		int expectedNumber=0;
		int actualNumber = userService.deleteUser("notusername");
		
		assertEquals(expectedNumber, actualNumber);
	}
	
//	@Test
//	public void deleteUser_shouldDeleteOneUser_whenUserExist() {
//		int actualCount = userService.deleteUser("usertodelete");
//		int expectedCount = 1;
//		
//		assertEquals(expectedCount, actualCount);
//	}


}
