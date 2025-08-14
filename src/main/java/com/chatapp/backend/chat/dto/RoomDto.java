package com.chatapp.backend.chat.dto;

import com.chatapp.backend.chat.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    
    private UUID id;
    private String name;
    private String description;
    private String avatarUrl;
    private Room.RoomType roomType;
    private UUID createdById;
    private String createdByUsername;
    private String createdByAvatar;
    private List<RoomMemberDto> members;
    private List<RoomMemberDto> admins;
    private Boolean isPrivate;
    private Integer maxMembers;
    private Integer currentMemberCount;
    private LocalDateTime lastMessageAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for UI
    private Boolean isMember;
    private Boolean isAdmin;
    private Boolean isCreator;
    private Integer unreadMessageCount;
    
    // Static method để convert từ Entity sang DTO
    public static RoomDto fromEntity(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .description(room.getDescription())
                .avatarUrl(room.getAvatarUrl())
                .roomType(room.getRoomType())
                .createdById(room.getCreatedBy().getId())
                .createdByUsername(room.getCreatedBy().getUsername())
                .createdByAvatar(room.getCreatedBy().getAvatar())
                .isPrivate(room.getIsPrivate())
                .maxMembers(room.getMaxMembers())
                .currentMemberCount(room.getCurrentMemberCount())
                .lastMessageAt(room.getLastMessageAt())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
    
    // DTO cho room member
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomMemberDto {
        private UUID id;
        private String username;
        private String email;
        private String avatar;
        private LocalDateTime joinedAt;
    }
}

