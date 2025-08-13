package com.chatapp.backend.common.dto;

import com.chatapp.backend.common.constants.MessageConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API Response Template")
public class ApiResponse<T> {
    
    @Schema(description = "Success status", example = "true")
    private boolean success;
    
    @Schema(description = "HTTP status code", example = "200")
    private int code;
    
    @Schema(description = "Response message", example = "Operation successful")
    private String message;
    
    @Schema(description = "Response timestamp", example = "2024-01-01T12:00:00")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    @Schema(description = "Response data")
    private T data;
    
    @Schema(description = "Pagination information")
    private PaginationInfo pagination;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Pagination information")
    public static class PaginationInfo {
        @Schema(description = "Current page number", example = "1")
        private int page;
        
        @Schema(description = "Number of items per page", example = "10")
        private int limit;
        
        @Schema(description = "Total number of items", example = "100")
        private long total;
        
        @Schema(description = "Total number of pages", example = "10")
        private int totalPages;
        
        @Schema(description = "Has next page", example = "true")
        private boolean hasNext;
        
        @Schema(description = "Has previous page", example = "false")
        private boolean hasPrevious;
    }
    
    // Static factory methods for common responses
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(200)
                .message(MessageConstants.SUCCESS_OPERATION)
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(200)
                .message(message)
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> success(T data, int code, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(201)
                .message(MessageConstants.SUCCESS_CREATED)
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(201)
                .message(message)
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> noContent() {
        return ApiResponse.<T>builder()
                .success(true)
                .code(204)
                .message(MessageConstants.SUCCESS_DELETED)
                .build();
    }
    
    public static <T> ApiResponse<T> noContent(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(204)
                .message(message)
                .build();
    }
    
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }
    
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(400, message);
    }
    
    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(401, message);
    }
    
    public static <T> ApiResponse<T> forbidden(String message) {
        return error(403, message);
    }
    
    public static <T> ApiResponse<T> notFound(String message) {
        return error(404, message);
    }
    
    public static <T> ApiResponse<T> conflict(String message) {
        return error(409, message);
    }
    
    public static <T> ApiResponse<T> internalServerError(String message) {
        return error(500, message);
    }
}
