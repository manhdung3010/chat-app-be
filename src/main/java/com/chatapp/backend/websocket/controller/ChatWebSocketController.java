package com.chatapp.backend.websocket.controller;

import com.chatapp.backend.chat.dto.CreateMessageRequest;
import com.chatapp.backend.chat.service.MessageService;
import com.chatapp.backend.websocket.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

/**
 * WebSocket Controller để xử lý chat real-time
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Xử lý tin nhắn chat
     * Client gửi: /app/chat
     * Server gửi: /topic/public hoặc /user/{userId}/queue/messages
     */
    @MessageMapping("/chat")
    public void handleChatMessage(@Payload ChatMessage chatMessage, 
                                 SimpMessageHeaderAccessor headerAccessor) {
        
        log.info("Received chat message: {}", chatMessage);
        
        try {
            // Lưu tin nhắn vào database
            CreateMessageRequest request = CreateMessageRequest.builder()
                    .content(chatMessage.getContent())
                    .receiverId(chatMessage.getReceiverId())
                    .roomId(chatMessage.getRoomId())
                    .messageType(chatMessage.getMessageType())
                    .build();
            
            // Lấy sender ID từ WebSocket session
            String senderIdStr = headerAccessor.getUser().getName();
            UUID senderId = UUID.fromString(senderIdStr);
            
            // Tạo tin nhắn trong database
            var messageResponse = messageService.createMessage(request, senderId);
            ChatMessage savedMessage = ChatMessage.fromMessageResponse(messageResponse);
            
            // Gửi tin nhắn đến người nhận
            if (chatMessage.getReceiverId() != null) {
                // Private chat - gửi đến user cụ thể
                messagingTemplate.convertAndSendToUser(
                        chatMessage.getReceiverId().toString(),
                        "/queue/messages",
                        savedMessage
                );
                
                // Gửi lại cho người gửi (confirmation)
                messagingTemplate.convertAndSendToUser(
                        senderId.toString(),
                        "/queue/messages",
                        savedMessage
                );
            } else if (chatMessage.getRoomId() != null) {
                // Group chat - gửi đến tất cả trong room
                messagingTemplate.convertAndSend(
                        "/topic/room/" + chatMessage.getRoomId(),
                        savedMessage
                );
            }
            
        } catch (Exception e) {
            log.error("Error processing chat message", e);
            
            // Gửi error message về cho sender
            ChatMessage errorMessage = ChatMessage.builder()
                    .type(ChatMessage.MessageType.CHAT)
                    .content("Error: " + e.getMessage())
                    .senderId(chatMessage.getSenderId())
                    .build();
            
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getSenderId().toString(),
                    "/queue/errors",
                    errorMessage
            );
        }
    }

    /**
     * User join chat
     * Client gửi: /app/join
     * Server gửi: /topic/public
     */
    @MessageMapping("/join")
    @SendTo("/topic/public")
    public ChatMessage handleUserJoin(@Payload ChatMessage chatMessage, 
                                    SimpMessageHeaderAccessor headerAccessor) {
        
        log.info("User joined: {}", chatMessage.getSenderUsername());
        
        // Thêm username vào WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderUsername());
        headerAccessor.getSessionAttributes().put("userId", chatMessage.getSenderId());
        
        return ChatMessage.builder()
                .type(ChatMessage.MessageType.JOIN)
                .senderId(chatMessage.getSenderId())
                .senderUsername(chatMessage.getSenderUsername())
                .content(chatMessage.getSenderUsername() + " joined the chat")
                .build();
    }

    /**
     * User typing indicator
     * Client gửi: /app/typing
     * Server gửi: /user/{receiverId}/queue/typing
     */
    @MessageMapping("/typing")
    public void handleTyping(@Payload ChatMessage chatMessage) {
        
        if (chatMessage.getReceiverId() != null) {
            // Gửi typing indicator đến người nhận
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getReceiverId().toString(),
                    "/queue/typing",
                    ChatMessage.builder()
                            .type(ChatMessage.MessageType.TYPING)
                            .senderId(chatMessage.getSenderId())
                            .senderUsername(chatMessage.getSenderUsername())
                            .build()
            );
        }
    }

    /**
     * User stop typing indicator
     * Client gửi: /app/stop-typing
     * Server gửi: /user/{receiverId}/queue/typing
     */
    @MessageMapping("/stop-typing")
    public void handleStopTyping(@Payload ChatMessage chatMessage) {
        
        if (chatMessage.getReceiverId() != null) {
            // Gửi stop typing indicator đến người nhận
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getReceiverId().toString(),
                    "/queue/typing",
                    ChatMessage.builder()
                            .type(ChatMessage.MessageType.STOP_TYPING)
                            .senderId(chatMessage.getSenderId())
                            .senderUsername(chatMessage.getSenderUsername())
                            .build()
            );
        }
    }

    /**
     * Mark message as read
     * Client gửi: /app/read
     * Server gửi: /user/{senderId}/queue/read
     */
    @MessageMapping("/read")
    public void handleMessageRead(@Payload ChatMessage chatMessage, 
                                SimpMessageHeaderAccessor headerAccessor) {
        
        String currentUserIdStr = headerAccessor.getUser().getName();
        UUID currentUserId = UUID.fromString(currentUserIdStr);
        
        // Đánh dấu tin nhắn đã đọc
        messageService.markMessagesAsRead(currentUserId, chatMessage.getSenderId());
        
        // Gửi confirmation đến người gửi
        messagingTemplate.convertAndSendToUser(
                chatMessage.getSenderId().toString(),
                "/queue/read",
                ChatMessage.builder()
                        .type(ChatMessage.MessageType.READ)
                        .senderId(currentUserId)
                        .receiverId(chatMessage.getSenderId())
                        .build()
            );
    }
}
