package com.chatapp.backend.chat.controller;

import com.chatapp.backend.chat.dto.CreateMessageRequest;
import com.chatapp.backend.chat.dto.MessageResponse;
import com.chatapp.backend.chat.service.MessageService;
import com.chatapp.backend.common.annotations.CurrentUserId;
import com.chatapp.backend.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller cho chức năng chat
 */
@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat", description = "Chat management APIs")
public class ChatController {

    private final MessageService messageService;

    /**
     * Lấy danh sách tin nhắn chưa đọc của user hiện tại
     */
    @GetMapping("/unread")
    @Operation(summary = "Get unread messages", description = "Get all unread messages for current user")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getUnreadMessages(
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Getting unread messages for user: {}", currentUserId);
        
        List<MessageResponse> unreadMessages = messageService.getUnreadMessages(currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<List<MessageResponse>>builder()
                .success(true)
                .message("Unread messages retrieved successfully")
                .data(unreadMessages)
                .build());
    }

    /**
     * Lấy số lượng tin nhắn chưa đọc
     */
    @GetMapping("/unread/count")
    @Operation(summary = "Get unread message count", description = "Get count of unread messages for current user")
    public ResponseEntity<ApiResponse<Long>> getUnreadMessageCount(
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Getting unread message count for user: {}", currentUserId);
        
        long count = messageService.getUnreadMessageCount(currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<Long>builder()
                .success(true)
                .message("Unread message count retrieved successfully")
                .data(count)
                .build());
    }

    /**
     * Lấy tin nhắn giữa 2 user (private chat)
     */
    @GetMapping("/conversation/{otherUserId}")
    @Operation(summary = "Get conversation messages", description = "Get messages between current user and another user")
    public ResponseEntity<ApiResponse<Page<MessageResponse>>> getConversationMessages(
            @PathVariable UUID otherUserId,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        log.info("Getting conversation messages between user {} and {}", currentUserId, otherUserId);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<MessageResponse> messages = messageService.getMessagesBetweenUsers(currentUserId, otherUserId, pageable);
        
        return ResponseEntity.ok(ApiResponse.<Page<MessageResponse>>builder()
                .success(true)
                .message("Conversation messages retrieved successfully")
                .data(messages)
                .build());
    }

    /**
     * Lấy tin nhắn trong room (group chat)
     */
    @GetMapping("/room/{roomId}")
    @Operation(summary = "Get room messages", description = "Get messages in a specific chat room")
    public ResponseEntity<ApiResponse<Page<MessageResponse>>> getRoomMessages(
            @PathVariable UUID roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        log.info("Getting messages for room: {}", roomId);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<MessageResponse> messages = messageService.getMessagesByRoomId(roomId, pageable);
        
        return ResponseEntity.ok(ApiResponse.<Page<MessageResponse>>builder()
                .success(true)
                .message("Room messages retrieved successfully")
                .data(messages)
                .build());
    }

    /**
     * Lấy tin nhắn gần nhất của mỗi cuộc trò chuyện
     */
    @GetMapping("/conversations/latest")
    @Operation(summary = "Get latest messages", description = "Get latest message from each conversation")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getLatestMessages(
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Getting latest messages for user: {}", currentUserId);
        
        List<MessageResponse> latestMessages = messageService.getLatestMessagesForUser(currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<List<MessageResponse>>builder()
                .success(true)
                .message("Latest messages retrieved successfully")
                .data(latestMessages)
                .build());
    }

    /**
     * Tạo tin nhắn mới
     */
    @PostMapping
    @Operation(summary = "Create new message", description = "Create a new message")
    public ResponseEntity<ApiResponse<MessageResponse>> createMessage(
            @Valid @RequestBody CreateMessageRequest request,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Creating new message from user: {}", currentUserId);
        
        MessageResponse message = messageService.createMessage(request, currentUserId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<MessageResponse>builder()
                        .success(true)
                        .message("Message created successfully")
                        .data(message)
                        .build());
    }

    /**
     * Đánh dấu tin nhắn đã đọc
     */
    @PutMapping("/read/{senderId}")
    @Operation(summary = "Mark messages as read", description = "Mark messages from a specific sender as read")
    public ResponseEntity<ApiResponse<Void>> markMessagesAsRead(
            @PathVariable UUID senderId,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Marking messages as read from sender: {} for user: {}", senderId, currentUserId);
        
        messageService.markMessagesAsRead(currentUserId, senderId);
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Messages marked as read successfully")
                .build());
    }

    /**
     * Xóa tin nhắn
     */
    @DeleteMapping("/{messageId}")
    @Operation(summary = "Delete message", description = "Delete a specific message")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(
            @PathVariable UUID messageId,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Deleting message: {} by user: {}", messageId, currentUserId);
        
        messageService.deleteMessage(messageId, currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Message deleted successfully")
                .build());
    }
}

