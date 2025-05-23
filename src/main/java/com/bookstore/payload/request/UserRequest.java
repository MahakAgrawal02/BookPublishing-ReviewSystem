package com.bookstore.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
	
	// Username must be non-null, non-blank, 4-20 characters, no special chars or spaces
	@NotBlank(message = "Invalid Username: Empty username")
	@NotNull(message = "Invalid UserName: username is NULL")
	@Size(min=4, max=20, message="Invalid Username: username must be between 4 and 20 characters")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username should not contain special characters or spaces")
	private String username;
	
	// Password must be non-null, non-blank, minimum 7 characters
	@NotBlank(message = "Invalid Password: Empty password")
	@NotNull(message = "Invalid Password: password is NULL")
	@Size(min = 7, message = "Invalid Password: password must be at least 7 characters")
	private String password;
	
	// Role assigned to the user; must be non-null and non-blank
	@NotBlank(message = "RoleName is mandatory")
	@NotNull(message = "Invalid role name: role is NULL")
	private String roleName;
	
	// Constructor for creating a user request with username and password only
	public UserRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
