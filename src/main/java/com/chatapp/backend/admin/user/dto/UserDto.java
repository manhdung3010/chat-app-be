package com.chatapp.backend.admin.user.dto;

import com.chatapp.backend.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User DTO for admin operations")
public class UserDto {
    
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
    
    @Schema(description = "User creation date")
    private LocalDateTime createdAt;
    
    @Schema(description = "User last update date")
    private LocalDateTime updatedAt;
}
