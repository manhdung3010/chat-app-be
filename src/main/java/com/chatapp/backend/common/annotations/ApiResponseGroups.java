package com.chatapp.backend.common.annotations;

import com.chatapp.backend.common.constants.HttpStatusCodes;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation composition for common API response patterns
 */
public class ApiResponseGroups {
    
    /**
     * Common responses for admin authentication (401, 403)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.UNAUTHORIZED, description = "Unauthorized - Bearer token required"),
        @ApiResponse(responseCode = HttpStatusCodes.FORBIDDEN, description = "Forbidden - Admin role required")
    })
    public @interface AdminAuthResponses {}
    
    /**
     * Common responses for admin CRUD operations (401, 403, 404)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.UNAUTHORIZED, description = "Unauthorized - Bearer token required"),
        @ApiResponse(responseCode = HttpStatusCodes.FORBIDDEN, description = "Forbidden - Admin role required"),
        @ApiResponse(responseCode = HttpStatusCodes.NOT_FOUND, description = "User not found")
    })
    public @interface AdminCrudResponses {}
    
    /**
     * Common responses for admin create operations (401, 403, 400, 409)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.UNAUTHORIZED, description = "Unauthorized - Bearer token required"),
        @ApiResponse(responseCode = HttpStatusCodes.FORBIDDEN, description = "Forbidden - Admin role required"),
        @ApiResponse(responseCode = HttpStatusCodes.BAD_REQUEST, description = "Bad request - validation failed"),
        @ApiResponse(responseCode = HttpStatusCodes.CONFLICT, description = "Conflict - username or email already exists")
    })
    public @interface AdminCreateResponses {}
    
    /**
     * Common responses for admin update operations (401, 403, 400, 404, 409)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.UNAUTHORIZED, description = "Unauthorized - Bearer token required"),
        @ApiResponse(responseCode = HttpStatusCodes.FORBIDDEN, description = "Forbidden - Admin role required"),
        @ApiResponse(responseCode = HttpStatusCodes.BAD_REQUEST, description = "Bad request - validation failed"),
        @ApiResponse(responseCode = HttpStatusCodes.NOT_FOUND, description = "User not found"),
        @ApiResponse(responseCode = HttpStatusCodes.CONFLICT, description = "Conflict - username or email already exists")
    })
    public @interface AdminUpdateResponses {}
    
    /**
     * Common responses for authentication operations (400, 401, 409)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.BAD_REQUEST, description = "Bad request - validation failed"),
        @ApiResponse(responseCode = HttpStatusCodes.UNAUTHORIZED, description = "Invalid credentials"),
        @ApiResponse(responseCode = HttpStatusCodes.CONFLICT, description = "Conflict - username or email already exists")
    })
    public @interface AuthResponses {}
    
    /**
     * Common responses for public operations (400, 500)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.BAD_REQUEST, description = "Bad request - validation failed"),
        @ApiResponse(responseCode = HttpStatusCodes.INTERNAL_SERVER_ERROR, description = "Internal server error")
    })
    public @interface PublicResponses {}
    
    /**
     * Common responses for validation errors (400)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.BAD_REQUEST, description = "Validation failed")
    })
    public @interface ValidationErrorResponse {}
    
    /**
     * Common responses for authentication errors (401)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.UNAUTHORIZED, description = "Invalid credentials")
    })
    public @interface AuthenticationErrorResponse {}
    
    /**
     * Common responses for not found errors (404)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.NOT_FOUND, description = "User not found")
    })
    public @interface NotFoundErrorResponse {}
    
    /**
     * Common responses for runtime errors (400)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.BAD_REQUEST, description = "Runtime error")
    })
    public @interface RuntimeErrorResponse {}
    
    /**
     * Common responses for server errors (500)
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusCodes.INTERNAL_SERVER_ERROR, description = "Internal server error")
    })
    public @interface ServerErrorResponse {}
}
