package com.chatapp.backend.auth.dto;

import com.chatapp.backend.common.constants.AppConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User login request")
public class LoginRequest {
    
    @NotBlank(message = AppConstants.USERNAME_OR_EMAIL_REQUIRED)
    @Schema(description = "Username or email address", example = "john_doe")
    private String usernameOrEmail;
    
    @NotBlank(message = AppConstants.PASSWORD_REQUIRED)
    @Schema(description = "User password", example = "password123")
    private String password;
}
