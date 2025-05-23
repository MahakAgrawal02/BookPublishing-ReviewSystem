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

    // Secret key for signing the JWT, injected from application properties (base64 encoded)
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    
    // JWT expiration time in milliseconds, injected from application properties
    @Value("${jwt.expiration}")
    private int JWT_EXPIRATION;
    
    /**
     * Generates a JWT token using the authenticated user's username.
     * Sets issued date and expiration date, then signs with the secret key.
     * 
     * @param authentication Authentication object containing user details
     * @return generated JWT token as a String
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())           // set username as subject
                .setIssuedAt(new Date())                            // current time as issued date
                .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION))  // expiration time
                .signWith(key(), SignatureAlgorithm.HS256)         // sign with HMAC SHA256 and secret key
                .compact();
    }
    
    /**
     * Constructs the signing key from the base64 encoded SECRET_KEY.
     * 
     * @return Key object for signing JWTs
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }
    
    /**
     * Extracts the username (subject) from the JWT token.
     * 
     * @param token JWT token string
     * @return username extracted from token
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    /**
     * Validates the JWT token's structure, signature, and expiration.
     * Throws specific exceptions for different validation failures.
     * 
     * @param authToken JWT token string
     * @return true if token is valid
     * @throws Exception for any validation failure with a detailed message
     */
    public boolean validateJwtToken(String authToken) throws Exception {
        try {
            // Parse token to validate signature and claims
            Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(authToken);
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
