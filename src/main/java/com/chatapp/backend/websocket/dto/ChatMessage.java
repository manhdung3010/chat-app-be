package com.chatapp.backend.websocket.dto;

import com.chatapp.backend.chat.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO cho WebSocket chat message
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    
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
    
    // WebSocket specific fields
    private MessageType type;  // CHAT, JOIN, LEAVE, TYPING, etc.
    private String sessionId;  // WebSocket session ID
    
    public enum MessageType {
        CHAT,       // Tin nhắn thông thường
        JOIN,       // User join chat
        LEAVE,      // User leave chat
        TYPING,     // User đang typing
        STOP_TYPING, // User stop typing
        READ,       // Tin nhắn đã đọc
        DELIVERED   // Tin nhắn đã gửi
    }
    
    // Convert từ Message entity
    public static ChatMessage fromMessage(Message message) {
        return ChatMessage.builder()
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
                .type(MessageType.CHAT)
                .build();
    }
    
    // Convert từ MessageResponse DTO
    public static ChatMessage fromMessageResponse(com.chatapp.backend.chat.dto.MessageResponse messageResponse) {
        return ChatMessage.builder()
                .id(messageResponse.getId())
                .content(messageResponse.getContent())
                .senderId(messageResponse.getSenderId())
                .senderUsername(messageResponse.getSenderUsername())
                .senderAvatar(messageResponse.getSenderAvatar())
                .receiverId(messageResponse.getReceiverId())
                .receiverUsername(messageResponse.getReceiverUsername())
                .receiverAvatar(messageResponse.getReceiverAvatar())
                .roomId(messageResponse.getRoomId())
                .messageType(messageResponse.getMessageType())
                .isRead(messageResponse.getIsRead())
                .createdAt(messageResponse.getCreatedAt())
                .updatedAt(messageResponse.getUpdatedAt())
                .type(MessageType.CHAT)
                .build();
    }
}
