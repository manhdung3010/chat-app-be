package com.chatapp.backend.auth.dto;

import com.chatapp.backend.common.constants.AppConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User registration request")
public class RegisterRequest {
    
    @NotBlank(message = AppConstants.USERNAME_REQUIRED)
    @Size(min = 3, max = 50, message = AppConstants.USERNAME_LENGTH_CONSTRAINT)
    @Schema(description = "Username for the account", example = "john_doe", minLength = 3, maxLength = 50)
    private String username;
    
    @NotBlank(message = AppConstants.EMAIL_REQUIRED)
    @Email(message = AppConstants.INVALID_EMAIL_FORMAT)
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;
    
    @NotBlank(message = AppConstants.PASSWORD_REQUIRED)
    @Size(min = 6, message = AppConstants.PASSWORD_LENGTH_CONSTRAINT)
    @Schema(description = "Password for the account", example = "password123", minLength = 6)
    private String password;
    
    @Schema(description = "Avatar URL (optional)", example = "https://example.com/avatar.jpg")
    private String avatar;
}
