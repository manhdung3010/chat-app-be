package com.chatapp.backend.auth.controller;

import com.chatapp.backend.auth.dto.AuthResponse;
import com.chatapp.backend.auth.dto.LoginRequest;
import com.chatapp.backend.auth.dto.RegisterRequest;
import com.chatapp.backend.auth.dto.RefreshTokenRequest;
import com.chatapp.backend.auth.service.AuthenticationService;
import com.chatapp.backend.common.annotations.ApiResponseGroups;
import com.chatapp.backend.common.constants.AppConstants;
import com.chatapp.backend.common.constants.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "User registered successfully")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user with username/email and password")
    @ApiResponseGroups.AuthResponses
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "User logged in successfully")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Get new access token using refresh token")
    @ApiResponseGroups.AuthResponses
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Token refreshed successfully")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}
