package com.chatapp.backend.chat.repository;

import com.chatapp.backend.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    
    // Tìm tin nhắn giữa 2 user (private chat)
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR " +
           "(m.sender.id = :user2Id AND m.receiver.id = :user1Id) " +
           "ORDER BY m.createdAt DESC")
    Page<Message> findMessagesBetweenUsers(
            @Param("user1Id") UUID user1Id,
            @Param("user2Id") UUID user2Id,
            Pageable pageable
    );
    
    // Tìm tin nhắn trong room (group chat)
    @Query("SELECT m FROM Message m WHERE m.roomId = :roomId ORDER BY m.createdAt DESC")
    Page<Message> findMessagesByRoomId(
            @Param("roomId") UUID roomId,
            Pageable pageable
    );
    
    // Đếm tin nhắn chưa đọc của user
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id = :userId AND m.isRead = false")
    long countUnreadMessages(@Param("userId") UUID userId);
    
    // Tìm tin nhắn chưa đọc của user
    @Query("SELECT m FROM Message m WHERE m.receiver.id = :userId AND m.isRead = false ORDER BY m.createdAt DESC")
    List<Message> findUnreadMessages(@Param("userId") UUID userId);
    
    // Đánh dấu tin nhắn đã đọc
    @Query("UPDATE Message m SET m.isRead = true WHERE m.receiver.id = :userId AND m.sender.id = :senderId")
    void markMessagesAsRead(@Param("userId") UUID userId, @Param("senderId") UUID senderId);
    
    // Tìm tin nhắn gần nhất của mỗi cuộc trò chuyện
    @Query("SELECT DISTINCT m FROM Message m WHERE " +
           "m.id IN (" +
           "  SELECT MAX(m2.id) FROM Message m2 WHERE " +
           "  (m2.sender.id = :userId OR m2.receiver.id = :userId) " +
           "  GROUP BY " +
           "  CASE " +
           "    WHEN m2.sender.id = :userId THEN m2.receiver.id " +
           "    ELSE m2.sender.id " +
           "  END" +
           ") ORDER BY m.createdAt DESC")
    List<Message> findLatestMessagesForUser(@Param("userId") UUID userId);
}

