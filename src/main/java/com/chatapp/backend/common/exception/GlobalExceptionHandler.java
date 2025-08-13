package com.chatapp.backend.common.exception;

import com.chatapp.backend.common.annotations.ApiResponseGroups;
import com.chatapp.backend.common.constants.MessageConstants;
import com.chatapp.backend.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice()
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponseGroups.ValidationErrorResponse
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .code(400)
                        .message(MessageConstants.ERROR_VALIDATION)
                        .data(errors)
                        .build());
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    @ApiResponseGroups.AuthenticationErrorResponse
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.unauthorized(MessageConstants.AUTH_INVALID_CREDENTIALS));
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    @ApiResponseGroups.NotFoundErrorResponse
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound(ex.getMessage()));
    }
    
    @ExceptionHandler(RuntimeException.class)
    @ApiResponseGroups.RuntimeErrorResponse
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest(ex.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    @ApiResponseGroups.ServerErrorResponse
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.internalServerError(MessageConstants.ERROR_SERVER));
    }
}
