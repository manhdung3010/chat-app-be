package com.chatapp.backend.user.dto;

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
@Schema(description = "User list item DTO for chat purposes")
public class UserListItemDto {
    
    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    @Schema(description = "Username", example = "john_doe")
    private String username;
    
    @Schema(description = "Avatar URL", example = "https://example.com/avatar.jpg")
    private String avatar;
    
    @Schema(description = "User creation date")
    private LocalDateTime createdAt;
}
