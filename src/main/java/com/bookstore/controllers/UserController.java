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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.payload.request.UserRequest;
import com.bookstore.payload.response.JwtResponse;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.services.UserDetailsImpl;
import com.bookstore.services.UserDetailsServiceImpl;
import com.bookstore.utils.JwtUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserRepository userRepo;
	@Autowired
	RoleRepository roleRepo;
	@Autowired
	UserDetailsServiceImpl userService;
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody UserRequest userInfo){
		
		String roleName;
		String roleInput = userInfo.getRoleName().toUpperCase();
		if(roleInput.equals("ADMIN")) {
			roleName = "ADMIN";
		}
		else {
			roleName = "USER";
		}
		
		userInfo.setRoleName(roleName);
		
		boolean isSaved = userService.saveUser(userInfo);
		
		if(!isSaved) {
			
			String message = "User with username=" + "already exists. Please, log in.";
			return new ResponseEntity<String>(message, HttpStatus.CONFLICT);
		}
		else {
			return new ResponseEntity<UserRequest>(userInfo, HttpStatus.CREATED);
		}
		

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserRequest userInfo){

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
				.collect(Collectors.toList());
		
		return new ResponseEntity<>(new JwtResponse(jwt), HttpStatus.OK);
		
	}

}
