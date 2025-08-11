package com.chatapp.backend.chat.dto;

import com.chatapp.backend.chat.entity.Message;
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
public class MessageResponse {
    
    private UUID id;
    private String content;
    private UUID senderId;
    private String senderUsername;
    private String senderAvatar;
    private UUID receiverId;
    private String receiverUsername;
    private String receiverAvatar;
    private UUID roomId;
    private Message.MessageType messageType;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Static method để convert từ Entity sang DTO
    public static MessageResponse fromEntity(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .senderAvatar(message.getSender().getAvatar())
                .receiverId(message.getReceiver() != null ? message.getReceiver().getId() : null)
                .receiverUsername(message.getReceiver() != null ? message.getReceiver().getUsername() : null)
                .receiverAvatar(message.getReceiver() != null ? message.getReceiver().getAvatar() : null)
                .roomId(message.getRoomId())
                .messageType(message.getMessageType())
                .isRead(message.getIsRead())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}

