package com.chatapp.backend.chat.repository;

import com.chatapp.backend.chat.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    
    // Tìm tất cả phòng mà user là thành viên
    @Query("SELECT DISTINCT r FROM Room r " +
           "JOIN r.members m " +
           "WHERE m.id = :userId " +
           "ORDER BY r.lastMessageAt DESC NULLS LAST, r.updatedAt DESC")
    Page<Room> findRoomsByMemberId(@Param("userId") UUID userId, Pageable pageable);
    
    // Tìm phòng private giữa 2 user
    @Query("SELECT r FROM Room r " +
           "WHERE r.roomType = 'PRIVATE' " +
           "AND r.currentMemberCount = 2 " +
           "AND EXISTS (SELECT 1 FROM r.members m WHERE m.id = :user1Id) " +
           "AND EXISTS (SELECT 1 FROM r.members m WHERE m.id = :user2Id)")
    Optional<Room> findPrivateRoomBetweenUsers(
            @Param("user1Id") UUID user1Id, 
            @Param("user2Id") UUID user2Id
    );
    
    // Tìm phòng theo tên (cho search)
    @Query("SELECT r FROM Room r " +
           "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "AND r.isPrivate = false " +
           "ORDER BY r.name")
    Page<Room> findPublicRoomsByNameContaining(
            @Param("searchTerm") String searchTerm, 
            Pageable pageable
    );
    
    // Tìm phòng mà user có thể join (public rooms)
    @Query("SELECT r FROM Room r " +
           "WHERE r.isPrivate = false " +
           "AND r.roomType = 'GROUP' " +
           "AND (r.maxMembers IS NULL OR r.currentMemberCount < r.maxMembers) " +
           "AND NOT EXISTS (SELECT 1 FROM r.members m WHERE m.id = :userId) " +
           "ORDER BY r.currentMemberCount DESC, r.createdAt DESC")
    Page<Room> findJoinablePublicRooms(@Param("userId") UUID userId, Pageable pageable);
    
    // Tìm phòng mà user là admin
    @Query("SELECT r FROM Room r " +
           "JOIN r.admins a " +
           "WHERE a.id = :userId " +
           "ORDER BY r.updatedAt DESC")
    List<Room> findRoomsByAdminId(@Param("userId") UUID userId);
    
    // Tìm phòng mà user tạo
    @Query("SELECT r FROM Room r " +
           "WHERE r.createdBy.id = :userId " +
           "ORDER BY r.createdAt DESC")
    List<Room> findRoomsByCreatorId(@Param("userId") UUID userId);
    
    // Đếm số phòng mà user là thành viên
    @Query("SELECT COUNT(DISTINCT r) FROM Room r " +
           "JOIN r.members m " +
           "WHERE m.id = :userId")
    long countRoomsByMemberId(@Param("userId") UUID userId);
    
    // Tìm phòng có tin nhắn gần đây nhất
    @Query("SELECT r FROM Room r " +
           "JOIN r.members m " +
           "WHERE m.id = :userId " +
           "AND r.lastMessageAt IS NOT NULL " +
           "ORDER BY r.lastMessageAt DESC")
    List<Room> findRecentRoomsByMemberId(@Param("userId") UUID userId, Pageable pageable);
    
    // Kiểm tra user có phải là thành viên của phòng không
    @Query("SELECT COUNT(r) > 0 FROM Room r " +
           "JOIN r.members m " +
           "WHERE r.id = :roomId AND m.id = :userId")
    boolean isUserMemberOfRoom(@Param("roomId") UUID roomId, @Param("userId") UUID userId);
    
    // Kiểm tra user có phải là admin của phòng không
    @Query("SELECT COUNT(r) > 0 FROM Room r " +
           "JOIN r.admins a " +
           "WHERE r.id = :roomId AND a.id = :userId")
    boolean isUserAdminOfRoom(@Param("roomId") UUID roomId, @Param("userId") UUID userId);
}

