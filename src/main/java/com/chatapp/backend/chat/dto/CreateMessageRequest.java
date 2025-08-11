package com.chatapp.backend.chat.dto;

import com.chatapp.backend.chat.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessageRequest {
    
    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    private String content;
    
    private UUID receiverId; // Cho private chat
    
    private UUID roomId; // Cho group chat
    
    @Builder.Default
    private Message.MessageType messageType = Message.MessageType.TEXT;
}

