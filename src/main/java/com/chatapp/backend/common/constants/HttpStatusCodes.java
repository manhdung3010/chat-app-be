package com.chatapp.backend.common.constants;

/**
 * HTTP Status Codes constants for use in API documentation
 */
public final class HttpStatusCodes {
    
    // Success responses
    public static final String OK = "200";
    public static final String CREATED = "201";
    public static final String NO_CONTENT = "204";
    
    // Client error responses
    public static final String BAD_REQUEST = "400";
    public static final String UNAUTHORIZED = "401";
    public static final String FORBIDDEN = "403";
    public static final String NOT_FOUND = "404";
    public static final String CONFLICT = "409";
    
    // Server error responses
    public static final String INTERNAL_SERVER_ERROR = "500";
    
    private HttpStatusCodes() {
        // Prevent instantiation
    }
}

