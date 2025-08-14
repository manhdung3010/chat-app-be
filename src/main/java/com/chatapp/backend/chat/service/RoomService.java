package com.chatapp.backend.chat.service;

import com.chatapp.backend.chat.dto.CreateRoomRequest;
import com.chatapp.backend.chat.dto.RoomDto;
import com.chatapp.backend.chat.entity.Room;
import com.chatapp.backend.chat.repository.MessageRepository;
import com.chatapp.backend.chat.repository.RoomRepository;
import com.chatapp.backend.user.entity.User;
import com.chatapp.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {
    
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    
    /**
     * Tạo phòng mới
     */
    public RoomDto createRoom(CreateRoomRequest request, UUID creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Người tạo phòng không tồn tại"));
        
        // Tạo phòng mới
        Room room = Room.builder()
                .name(request.getName())
                .description(request.getDescription())
                .avatarUrl(request.getAvatarUrl())
                .roomType(request.getRoomType())
                .createdBy(creator)
                .isPrivate(request.getIsPrivate())
                .maxMembers(request.getMaxMembers())
                .currentMemberCount(1) // Creator là member đầu tiên
                .build();
        
        // Thêm creator vào members và admins
        room.addMember(creator);
        room.addAdmin(creator);
        
        // Thêm các members khác nếu có
        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            for (UUID memberId : request.getMemberIds()) {
                if (!memberId.equals(creatorId)) {
                    User member = userRepository.findById(memberId)
                            .orElseThrow(() -> new RuntimeException("User không tồn tại: " + memberId));
                    room.addMember(member);
                }
            }
        }
        
        Room savedRoom = roomRepository.save(room);
        return RoomDto.fromEntity(savedRoom);
    }
    
    /**
     * Tạo hoặc lấy phòng private giữa 2 user
     */
    public RoomDto getOrCreatePrivateRoom(UUID user1Id, UUID user2Id) {
        // Kiểm tra xem đã có phòng private giữa 2 user chưa
        Optional<Room> existingRoom = roomRepository.findPrivateRoomBetweenUsers(user1Id, user2Id);
        
        if (existingRoom.isPresent()) {
            return RoomDto.fromEntity(existingRoom.get());
        }
        
        // Tạo phòng private mới
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User 1 không tồn tại"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User 2 không tồn tại"));
        
        Room privateRoom = Room.builder()
                .name("Private Chat") // Tên mặc định cho private chat
                .roomType(Room.RoomType.PRIVATE)
                .createdBy(user1)
                .isPrivate(true)
                .currentMemberCount(2)
                .build();
        
        privateRoom.addMember(user1);
        privateRoom.addMember(user2);
        
        Room savedRoom = roomRepository.save(privateRoom);
        return RoomDto.fromEntity(savedRoom);
    }
    
    /**
     * Lấy danh sách phòng của user
     */
    @Transactional(readOnly = true)
    public Page<RoomDto> getUserRooms(UUID userId, Pageable pageable) {
        Page<Room> rooms = roomRepository.findRoomsByMemberId(userId, pageable);
        return rooms.map(room -> {
            RoomDto dto = RoomDto.fromEntity(room);
            dto.setIsMember(true);
            dto.setIsAdmin(roomRepository.isUserAdminOfRoom(room.getId(), userId));
            dto.setIsCreator(room.getCreatedBy().getId().equals(userId));
            dto.setUnreadMessageCount((int) messageRepository.countUnreadMessages(userId));
            return dto;
        });
    }
    
    /**
     * Lấy thông tin phòng
     */
    @Transactional(readOnly = true)
    public RoomDto getRoomById(UUID roomId, UUID userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
        
        // Kiểm tra user có quyền xem phòng không
        if (!roomRepository.isUserMemberOfRoom(roomId, userId)) {
            throw new RuntimeException("Bạn không có quyền truy cập phòng này");
        }
        
        RoomDto dto = RoomDto.fromEntity(room);
        dto.setIsMember(true);
        dto.setIsAdmin(roomRepository.isUserAdminOfRoom(roomId, userId));
        dto.setIsCreator(room.getCreatedBy().getId().equals(userId));
        dto.setUnreadMessageCount((int) messageRepository.countUnreadMessages(userId));
        
        return dto;
    }
    
    /**
     * Tìm phòng public
     */
    @Transactional(readOnly = true)
    public Page<RoomDto> searchPublicRooms(String searchTerm, Pageable pageable) {
        Page<Room> rooms = roomRepository.findPublicRoomsByNameContaining(searchTerm, pageable);
        return rooms.map(RoomDto::fromEntity);
    }
    
    /**
     * Lấy danh sách phòng có thể join
     */
    @Transactional(readOnly = true)
    public Page<RoomDto> getJoinableRooms(UUID userId, Pageable pageable) {
        Page<Room> rooms = roomRepository.findJoinablePublicRooms(userId, pageable);
        return rooms.map(RoomDto::fromEntity);
    }
    
    /**
     * Join phòng
     */
    public void joinRoom(UUID roomId, UUID userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        // Kiểm tra phòng có phải public không
        if (room.getIsPrivate()) {
            throw new RuntimeException("Không thể join phòng private");
        }
        
        // Kiểm tra user đã là member chưa
        if (roomRepository.isUserMemberOfRoom(roomId, userId)) {
            throw new RuntimeException("Bạn đã là thành viên của phòng này");
        }
        
        // Kiểm tra số lượng member
        if (room.getMaxMembers() != null && room.getCurrentMemberCount() >= room.getMaxMembers()) {
            throw new RuntimeException("Phòng đã đầy");
        }
        
        room.addMember(user);
        roomRepository.save(room);
    }
    
    /**
     * Leave phòng
     */
    public void leaveRoom(UUID roomId, UUID userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        // Kiểm tra user có phải là member không
        if (!roomRepository.isUserMemberOfRoom(roomId, userId)) {
            throw new RuntimeException("Bạn không phải thành viên của phòng này");
        }
        
        // Không cho phép creator leave phòng
        if (room.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Người tạo phòng không thể rời phòng");
        }
        
        room.removeMember(user);
        room.removeAdmin(user);
        roomRepository.save(room);
    }
    
    /**
     * Thêm member vào phòng (chỉ admin mới được)
     */
    public void addMemberToRoom(UUID roomId, UUID userId, UUID adminId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
        
        // Kiểm tra quyền admin
        if (!roomRepository.isUserAdminOfRoom(roomId, adminId)) {
            throw new RuntimeException("Bạn không có quyền thêm thành viên");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        // Kiểm tra user đã là member chưa
        if (roomRepository.isUserMemberOfRoom(roomId, userId)) {
            throw new RuntimeException("User đã là thành viên của phòng này");
        }
        
        // Kiểm tra số lượng member
        if (room.getMaxMembers() != null && room.getCurrentMemberCount() >= room.getMaxMembers()) {
            throw new RuntimeException("Phòng đã đầy");
        }
        
        room.addMember(user);
        roomRepository.save(room);
    }
    
    /**
     * Xóa member khỏi phòng (chỉ admin mới được)
     */
    public void removeMemberFromRoom(UUID roomId, UUID userId, UUID adminId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
        
        // Kiểm tra quyền admin
        if (!roomRepository.isUserAdminOfRoom(roomId, adminId)) {
            throw new RuntimeException("Bạn không có quyền xóa thành viên");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        // Không cho phép xóa creator
        if (room.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Không thể xóa người tạo phòng");
        }
        
        // Không cho phép xóa chính mình
        if (userId.equals(adminId)) {
            throw new RuntimeException("Không thể xóa chính mình");
        }
        
        room.removeMember(user);
        room.removeAdmin(user);
        roomRepository.save(room);
    }
    
    /**
     * Cập nhật thông tin phòng (chỉ admin mới được)
     */
    public RoomDto updateRoom(UUID roomId, CreateRoomRequest request, UUID adminId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
        
        // Kiểm tra quyền admin
        if (!roomRepository.isUserAdminOfRoom(roomId, adminId)) {
            throw new RuntimeException("Bạn không có quyền cập nhật phòng");
        }
        
        room.setName(request.getName());
        room.setDescription(request.getDescription());
        room.setAvatarUrl(request.getAvatarUrl());
        room.setMaxMembers(request.getMaxMembers());
        
        Room savedRoom = roomRepository.save(room);
        return RoomDto.fromEntity(savedRoom);
    }
    
    /**
     * Xóa phòng (chỉ creator mới được)
     */
    public void deleteRoom(UUID roomId, UUID userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
        
        // Kiểm tra quyền creator
        if (!room.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Chỉ người tạo phòng mới có quyền xóa");
        }
        
        roomRepository.delete(room);
    }
    
    /**
     * Cập nhật thời gian tin nhắn cuối cùng
     */
    public void updateLastMessageTime(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
        
        room.setLastMessageAt(java.time.LocalDateTime.now());
        roomRepository.save(room);
    }
}

