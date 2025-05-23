package com.bookstore.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
	
	/**
	 * Handles unauthorized access attempts.
	 * Sends a JSON response with HTTP 401 status and error details.
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) 
			throws IOException, ServletException {
		
		// Set response content type to JSON
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// Set HTTP status code 401 (Unauthorized)
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		// Prepare response body with error details
		final Map<String, Object> body = new HashMap<>();
		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
	    body.put("error", "Unauthorized");
	    body.put("message", authException.getMessage());
	    body.put("path", request.getServletPath());
	    
	    // Write the response body as JSON to the output stream
	    final ObjectMapper mapper = new ObjectMapper();
	    mapper.writeValue(response.getOutputStream(), body);
	}

}
