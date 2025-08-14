package com.chatapp.backend.chat.controller;

import com.chatapp.backend.chat.dto.CreateRoomRequest;
import com.chatapp.backend.chat.dto.RoomDto;
import com.chatapp.backend.chat.service.RoomService;
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

import java.util.UUID;

/**
 * REST Controller cho quản lý phòng chat
 */
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Room Management", description = "Room management APIs")
public class RoomController {

    private final RoomService roomService;

    /**
     * Lấy danh sách phòng của user hiện tại
     */
    @GetMapping
    @Operation(summary = "Get user rooms", description = "Get all rooms that the current user is a member of")
    public ResponseEntity<ApiResponse<Page<RoomDto>>> getUserRooms(
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "lastMessageAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        log.info("Getting rooms for user: {}", currentUserId);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RoomDto> rooms = roomService.getUserRooms(currentUserId, pageable);
        
        return ResponseEntity.ok(ApiResponse.<Page<RoomDto>>builder()
                .success(true)
                .message("User rooms retrieved successfully")
                .data(rooms)
                .build());
    }

    /**
     * Lấy thông tin phòng
     */
    @GetMapping("/{roomId}")
    @Operation(summary = "Get room details", description = "Get detailed information about a specific room")
    public ResponseEntity<ApiResponse<RoomDto>> getRoomById(
            @PathVariable UUID roomId,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Getting room details for room: {} by user: {}", roomId, currentUserId);
        
        RoomDto room = roomService.getRoomById(roomId, currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<RoomDto>builder()
                .success(true)
                .message("Room details retrieved successfully")
                .data(room)
                .build());
    }

    /**
     * Tạo phòng mới
     */
    @PostMapping
    @Operation(summary = "Create new room", description = "Create a new chat room")
    public ResponseEntity<ApiResponse<RoomDto>> createRoom(
            @Valid @RequestBody CreateRoomRequest request,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Creating new room by user: {}", currentUserId);
        
        RoomDto room = roomService.createRoom(request, currentUserId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoomDto>builder()
                        .success(true)
                        .message("Room created successfully")
                        .data(room)
                        .build());
    }

    /**
     * Tạo hoặc lấy phòng private giữa 2 user
     */
    @PostMapping("/private/{otherUserId}")
    @Operation(summary = "Get or create private room", description = "Get or create a private room between current user and another user")
    public ResponseEntity<ApiResponse<RoomDto>> getOrCreatePrivateRoom(
            @PathVariable UUID otherUserId,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Getting or creating private room between user {} and {}", currentUserId, otherUserId);
        
        RoomDto room = roomService.getOrCreatePrivateRoom(currentUserId, otherUserId);
        
        return ResponseEntity.ok(ApiResponse.<RoomDto>builder()
                .success(true)
                .message("Private room retrieved/created successfully")
                .data(room)
                .build());
    }

    /**
     * Tìm phòng public
     */
    @GetMapping("/search")
    @Operation(summary = "Search public rooms", description = "Search for public rooms by name")
    public ResponseEntity<ApiResponse<Page<RoomDto>>> searchPublicRooms(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Searching public rooms with term: {}", searchTerm);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomDto> rooms = roomService.searchPublicRooms(searchTerm, pageable);
        
        return ResponseEntity.ok(ApiResponse.<Page<RoomDto>>builder()
                .success(true)
                .message("Public rooms search completed")
                .data(rooms)
                .build());
    }

    /**
     * Lấy danh sách phòng có thể join
     */
    @GetMapping("/joinable")
    @Operation(summary = "Get joinable rooms", description = "Get list of public rooms that user can join")
    public ResponseEntity<ApiResponse<Page<RoomDto>>> getJoinableRooms(
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Getting joinable rooms for user: {}", currentUserId);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomDto> rooms = roomService.getJoinableRooms(currentUserId, pageable);
        
        return ResponseEntity.ok(ApiResponse.<Page<RoomDto>>builder()
                .success(true)
                .message("Joinable rooms retrieved successfully")
                .data(rooms)
                .build());
    }

    /**
     * Join phòng
     */
    @PostMapping("/{roomId}/join")
    @Operation(summary = "Join room", description = "Join a public room")
    public ResponseEntity<ApiResponse<Void>> joinRoom(
            @PathVariable UUID roomId,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("User {} joining room: {}", currentUserId, roomId);
        
        roomService.joinRoom(roomId, currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Successfully joined the room")
                .build());
    }

    /**
     * Leave phòng
     */
    @PostMapping("/{roomId}/leave")
    @Operation(summary = "Leave room", description = "Leave a room")
    public ResponseEntity<ApiResponse<Void>> leaveRoom(
            @PathVariable UUID roomId,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("User {} leaving room: {}", currentUserId, roomId);
        
        roomService.leaveRoom(roomId, currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Successfully left the room")
                .build());
    }

    /**
     * Thêm member vào phòng (chỉ admin)
     */
    @PostMapping("/{roomId}/members/{userId}")
    @Operation(summary = "Add member to room", description = "Add a user to room (admin only)")
    public ResponseEntity<ApiResponse<Void>> addMemberToRoom(
            @PathVariable UUID roomId,
            @PathVariable UUID userId,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Admin {} adding user {} to room: {}", currentUserId, userId, roomId);
        
        roomService.addMemberToRoom(roomId, userId, currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Member added successfully")
                .build());
    }

    /**
     * Xóa member khỏi phòng (chỉ admin)
     */
    @DeleteMapping("/{roomId}/members/{userId}")
    @Operation(summary = "Remove member from room", description = "Remove a user from room (admin only)")
    public ResponseEntity<ApiResponse<Void>> removeMemberFromRoom(
            @PathVariable UUID roomId,
            @PathVariable UUID userId,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Admin {} removing user {} from room: {}", currentUserId, userId, roomId);
        
        roomService.removeMemberFromRoom(roomId, userId, currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Member removed successfully")
                .build());
    }

    /**
     * Cập nhật thông tin phòng (chỉ admin)
     */
    @PutMapping("/{roomId}")
    @Operation(summary = "Update room", description = "Update room information (admin only)")
    public ResponseEntity<ApiResponse<RoomDto>> updateRoom(
            @PathVariable UUID roomId,
            @Valid @RequestBody CreateRoomRequest request,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Admin {} updating room: {}", currentUserId, roomId);
        
        RoomDto room = roomService.updateRoom(roomId, request, currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<RoomDto>builder()
                .success(true)
                .message("Room updated successfully")
                .data(room)
                .build());
    }

    /**
     * Xóa phòng (chỉ creator)
     */
    @DeleteMapping("/{roomId}")
    @Operation(summary = "Delete room", description = "Delete a room (creator only)")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(
            @PathVariable UUID roomId,
            @Parameter(hidden = true) @CurrentUserId UUID currentUserId) {
        
        log.info("Creator {} deleting room: {}", currentUserId, roomId);
        
        roomService.deleteRoom(roomId, currentUserId);
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Room deleted successfully")
                .build());
    }
}

