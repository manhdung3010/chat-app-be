package com.chatapp.backend.common.constants;

public final class AppConstants {

    // API Paths
    public static final String API_BASE_PATH = "/api/v1";
    public static final String AUTH_PATH = API_BASE_PATH + "/auth";
    public static final String MESSAGE_PATH = API_BASE_PATH + "/messages";
    public static final String ADMIN_USER_PATH = API_BASE_PATH + "/admin/users";
    public static final String TEST_PATH = API_BASE_PATH + "/test";
    
    // Swagger/OpenAPI Paths
    public static final String SWAGGER_UI_PATH = "/swagger-ui";
    public static final String SWAGGER_UI_HTML_PATH = "/swagger-ui.html";
    public static final String API_DOCS_PATH = "/v3/api-docs";
    
    // Security Constants
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "Bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    
    // Error Messages
    public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid username or password";
    public static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred";
    public static final String VALIDATION_ERROR_MESSAGE = "Validation failed";
    
    // JWT Constants
    public static final String JWT_SECRET_PROPERTY = "jwt.secret";
    public static final String JWT_EXPIRATION_PROPERTY = "jwt.expiration";
    public static final String JWT_REFRESH_EXPIRATION_PROPERTY = "jwt.refresh-expiration";
    
    // Database Constants
    public static final String DATASOURCE_URL_PROPERTY = "spring.datasource.url";
    public static final String DATASOURCE_USERNAME_PROPERTY = "spring.datasource.username";
    public static final String DATASOURCE_PASSWORD_PROPERTY = "spring.datasource.password";
    
    // Redis Constants
    public static final String REDIS_HOST_PROPERTY = "redis.host";
    public static final String REDIS_PORT_PROPERTY = "redis.port";
    
    // Server Constants
    public static final String SERVER_PORT_PROPERTY = "server.port";
    public static final String APPLICATION_NAME_PROPERTY = "spring.application.name";
    
    // Validation Messages
    public static final String USERNAME_REQUIRED = "Username is required";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String USERNAME_OR_EMAIL_REQUIRED = "Username or email is required";
    public static final String INVALID_EMAIL_FORMAT = "Email should be valid";
    public static final String USERNAME_LENGTH_CONSTRAINT = "Username must be between 3 and 50 characters";
    public static final String PASSWORD_LENGTH_CONSTRAINT = "Password must be at least 6 characters";
    
    // HTTP Status Messages
    public static final String BAD_REQUEST_MESSAGE = "Bad Request";
    public static final String UNAUTHORIZED_MESSAGE = "Unauthorized";
    public static final String FORBIDDEN_MESSAGE = "Forbidden";
    public static final String NOT_FOUND_MESSAGE = "Not Found";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";
    
    private AppConstants() {
        // Private constructor to prevent instantiation
    }
}
