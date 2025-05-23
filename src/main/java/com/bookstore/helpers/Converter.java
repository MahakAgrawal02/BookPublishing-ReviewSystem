package com.bookstore.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter {
    
    /**
     * Converts an object to its JSON string representation.
     * 
     * @param obj Object to convert
     * @return JSON string representation of the object
     */
    public static String convertToString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON string", e);
        }
    }
    
    /**
     * Extracts the JWT token from the Authorization header.
     * Assumes the header starts with "Bearer " prefix.
     * 
     * @param header the Authorization header value
     * @return JWT token string without the "Bearer " prefix
     * @throws IllegalArgumentException if header is null or malformed
     */
    public static String getJwtTokenFromHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return header.substring(7);
    }
}
