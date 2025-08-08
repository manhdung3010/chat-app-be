package com.chatapp.backend.auth.dto;

import com.chatapp.backend.common.constants.AppConstants;
import com.chatapp.backend.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response")
public class AuthResponse {
    
    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    
    @Schema(description = "JWT refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
    
    @Schema(description = "Token type", example = "Bearer")
    @Builder.Default
    private String tokenType = AppConstants.TOKEN_TYPE;
    
    @Schema(description = "User information")
    private UserInfo user;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "User information")
    public static class UserInfo {
        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID id;
        
        @Schema(description = "Username", example = "john_doe")
        private String username;
        
        @Schema(description = "Email address", example = "john.doe@example.com")
        private String email;
        
        @Schema(description = "Avatar URL", example = "https://example.com/avatar.jpg")
        private String avatar;
        
        @Schema(description = "User role", example = "USER")
        private Role role;
    }
}
