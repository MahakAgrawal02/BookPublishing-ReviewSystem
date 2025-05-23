package com.bookstore.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bookstore.payload.request.UserRequest;
import com.bookstore.payload.response.JwtResponse;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.services.UserDetailsImpl;
import com.bookstore.services.UserDetailsServiceImpl;
import com.bookstore.utils.JwtUtil;

import jakarta.validation.Valid;

/**
 * REST controller to handle user authentication and registration.
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private UserDetailsServiceImpl userService;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * Registers a new user (admin or regular user).
	 *
	 * @param userInfo the user registration data
	 * @return the created user info or conflict message
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody UserRequest userInfo) {

		// Normalize role input
		String roleInput = userInfo.getRoleName().toUpperCase();
		String roleName = roleInput.equals("ADMIN") ? "ADMIN" : "USER";
		userInfo.setRoleName(roleName);

		boolean isSaved = userService.saveUser(userInfo);

		if (!isSaved) {
			String message = "User with username=" + userInfo.getUsername() + " already exists. Please, log in.";
			return new ResponseEntity<>(message, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(userInfo, HttpStatus.CREATED);
		}
	}

	/**
	 * Authenticates a user and generates a JWT token.
	 *
	 * @param userInfo the login credentials
	 * @return JWT token if login is successful
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserRequest userInfo) {

		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				userInfo.getUsername(),
				userInfo.getPassword()
			)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtil.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		userDetails.getAuthorities().stream()
			.map(item -> item.getAuthority())
			.collect(Collectors.toList()); // Authorities extracted, can be used if needed

		return new ResponseEntity<>(new JwtResponse(jwt), HttpStatus.OK);
	}
}
