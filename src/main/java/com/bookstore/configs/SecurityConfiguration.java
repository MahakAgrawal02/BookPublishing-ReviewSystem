package com.bookstore.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bookstore.security.AuthEntryPointJwt;
import com.bookstore.security.AuthTokenFilter;
import com.bookstore.services.UserDetailsServiceImpl;

/**
 * Security configuration class for setting up authentication and authorization.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	/**
	 * Bean for JWT authentication filter.
	 * Intercepts incoming requests to extract and validate JWT tokens.
	 */
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	/**
	 * Configures the authentication provider with user details and password encoder.
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(pwdEncoder);
		return authProvider;
	}

	/**
	 * Exposes the AuthenticationManager bean for use in authentication processes.
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * Defines the security filter chain for handling HTTP security.
	 * - Disables CSRF
	 * - Sets stateless session policy
	 * - Configures public and protected endpoints
	 * - Adds JWT token filter
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf -> csrf.disable())
			.exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(request -> request
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers("/api/test/**").permitAll()
				.requestMatchers("/swagger/**").permitAll()
				.requestMatchers("/book/**").authenticated()
				.anyRequest().authenticated()
			);

		httpSecurity.authenticationProvider(authenticationProvider());
		httpSecurity.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}
}
