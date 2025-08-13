package com.chatapp.backend.auth.controller;

import com.chatapp.backend.auth.dto.AuthResponse;
import com.chatapp.backend.auth.dto.LoginRequest;
import com.chatapp.backend.auth.dto.RegisterRequest;
import com.chatapp.backend.auth.dto.RefreshTokenRequest;
import com.chatapp.backend.auth.service.AuthenticationService;
import com.chatapp.backend.common.annotations.ApiResponseGroups;
import com.chatapp.backend.common.constants.AppConstants;
import com.chatapp.backend.common.constants.HttpStatusCodes;
import com.chatapp.backend.common.constants.MessageConstants;
import com.chatapp.backend.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstants.AUTH_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account with username, email, and password")
    @ApiResponseGroups.AuthResponses
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = HttpStatusCodes.CREATED, description = "User registered successfully")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(authResponse, MessageConstants.AUTH_REGISTER_SUCCESS));
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user with username/email and password")
    @ApiResponseGroups.AuthResponses
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = HttpStatusCodes.OK, description = "User logged in successfully")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authenticationService.login(request);
        return ResponseEntity.ok(ApiResponse.success(authResponse, MessageConstants.AUTH_LOGIN_SUCCESS));
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Get new access token using refresh token")
    @ApiResponseGroups.AuthResponses
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = HttpStatusCodes.OK, description = "Token refreshed successfully")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse authResponse = authenticationService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success(authResponse, MessageConstants.AUTH_REFRESH_SUCCESS));
    }
}
