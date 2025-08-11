package com.chatapp.backend.chat.controller;

import com.chatapp.backend.chat.dto.MessageResponse;
import com.chatapp.backend.chat.service.MessageService;
import com.chatapp.backend.common.annotations.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.chatapp.backend.common.constants.AppConstants;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(AppConstants.MESSAGE_PATH)
@RequiredArgsConstructor
@Tag(name = "Message", description = "Message management APIs")
public class MessageController {
    
    private final MessageService messageService;
    
    @GetMapping("/conversation/{userId}")
    @Operation(summary = "Lấy tin nhắn giữa 2 user")
    public ResponseEntity<Page<MessageResponse>> getMessagesBetweenUsers(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @CurrentUserId UUID currentUserId) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageResponse> messages = messageService.getMessagesBetweenUsers(currentUserId, userId, pageable);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/room/{roomId}")
    @Operation(summary = "Lấy tin nhắn trong room")
    public ResponseEntity<Page<MessageResponse>> getMessagesByRoomId(
            @PathVariable UUID roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageResponse> messages = messageService.getMessagesByRoomId(roomId, pageable);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/unread/count")
    @Operation(summary = "Lấy số tin nhắn chưa đọc")
    public ResponseEntity<Long> getUnreadMessageCount(@CurrentUserId UUID currentUserId) {
        long count = messageService.getUnreadMessageCount(currentUserId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/unread")
    @Operation(summary = "Lấy danh sách tin nhắn chưa đọc")
    public ResponseEntity<List<MessageResponse>> getUnreadMessages(@CurrentUserId UUID currentUserId) {
        List<MessageResponse> messages = messageService.getUnreadMessages(currentUserId);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/latest")
    @Operation(summary = "Lấy tin nhắn gần nhất của mỗi cuộc trò chuyện")
    public ResponseEntity<List<MessageResponse>> getLatestMessages(@CurrentUserId UUID currentUserId) {
        List<MessageResponse> messages = messageService.getLatestMessagesForUser(currentUserId);
        return ResponseEntity.ok(messages);
    }
    
    @DeleteMapping("/{messageId}")
    @Operation(summary = "Xóa tin nhắn")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable UUID messageId,
            @CurrentUserId UUID currentUserId) {
        
        messageService.deleteMessage(messageId, currentUserId);
        return ResponseEntity.ok().build();
    }
}

