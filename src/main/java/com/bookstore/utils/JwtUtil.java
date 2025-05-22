package com.bookstore.utils;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.bookstore.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String SECRET_KEY;
	
	@Value("${jwt.expiration}")
	private int JWT_EXPIRATION;
	
	public String generateJwtToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		
		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION))
				.signWith(key(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build()
				.parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateJwtToken(String authToken) throws Exception {
	    try {
	      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
	      return true;
	    } catch (MalformedJwtException e) {
	    	throw new MalformedJwtException("Invalid JWT token: " + e.getMessage());
	    } catch (ExpiredJwtException e) {
	    	throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "JWT token is expired: " + e.getMessage());
	    } catch (UnsupportedJwtException e) {
	    	throw new UnsupportedJwtException("JWT token is unsupported: " + e.getMessage());
	    } catch (IllegalArgumentException e) {
	    	throw new IllegalArgumentException("JWT claims string is empty: " + e.getMessage());
	    } catch (Exception e) {
	    	throw new Exception("Failed to validate token: " + e.getMessage());
	    }

	}

}
