package com.bookstore.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for defining the PasswordEncoder bean.
 * This ensures secure password hashing using BCrypt.
 */
@Configuration
public class PasswordConfig {

	/**
	 * Defines a BCrypt-based PasswordEncoder bean.
	 * This bean will be used throughout the application for encoding passwords.
	 *
	 * @return a BCryptPasswordEncoder instance
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
