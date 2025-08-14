package com.chatapp.backend.chat.service;

import com.chatapp.backend.chat.dto.CreateMessageRequest;
import com.chatapp.backend.chat.dto.MessageResponse;
import com.chatapp.backend.chat.entity.Message;
import com.chatapp.backend.chat.repository.MessageRepository;
import com.chatapp.backend.user.entity.User;
import com.chatapp.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RoomService roomService;
    
    /**
     * Tạo tin nhắn mới
     */
    public MessageResponse createMessage(CreateMessageRequest request, UUID senderId) {
        // Lấy thông tin người gửi
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Người gửi không tồn tại"));
        
        // Lấy thông tin người nhận (nếu có)
        User receiver = null;
        if (request.getReceiverId() != null) {
            receiver = userRepository.findById(request.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Người nhận không tồn tại"));
        }
        
        // Tạo tin nhắn mới
        Message message = Message.builder()
                .content(request.getContent())
                .sender(sender)
                .receiver(receiver)
                .roomId(request.getRoomId())
                .messageType(request.getMessageType())
                .isRead(false)
                .build();
        
        Message savedMessage = messageRepository.save(message);
        
        // Cập nhật thời gian tin nhắn cuối cùng của room nếu có
        if (request.getRoomId() != null) {
            roomService.updateLastMessageTime(request.getRoomId());
        }
        
        return MessageResponse.fromEntity(savedMessage);
    }
    
    /**
     * Lấy tin nhắn giữa 2 user
     */
    @Transactional(readOnly = true)
    public Page<MessageResponse> getMessagesBetweenUsers(UUID user1Id, UUID user2Id, Pageable pageable) {
        Page<Message> messages = messageRepository.findMessagesBetweenUsers(user1Id, user2Id, pageable);
        return messages.map(MessageResponse::fromEntity);
    }
    
    /**
     * Lấy tin nhắn trong room
     */
    @Transactional(readOnly = true)
    public Page<MessageResponse> getMessagesByRoomId(UUID roomId, Pageable pageable) {
        Page<Message> messages = messageRepository.findMessagesByRoomId(roomId, pageable);
        return messages.map(MessageResponse::fromEntity);
    }
    
    /**
     * Đánh dấu tin nhắn đã đọc
     */
    public void markMessagesAsRead(UUID userId, UUID senderId) {
        messageRepository.markMessagesAsRead(userId, senderId);
    }
    
    /**
     * Lấy số tin nhắn chưa đọc
     */
    @Transactional(readOnly = true)
    public long getUnreadMessageCount(UUID userId) {
        return messageRepository.countUnreadMessages(userId);
    }
    
    /**
     * Lấy danh sách tin nhắn chưa đọc
     */
    @Transactional(readOnly = true)
    public List<MessageResponse> getUnreadMessages(UUID userId) {
        List<Message> messages = messageRepository.findUnreadMessages(userId);
        return messages.stream()
                .map(MessageResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy tin nhắn gần nhất của mỗi cuộc trò chuyện
     */
    @Transactional(readOnly = true)
    public List<MessageResponse> getLatestMessagesForUser(UUID userId) {
        List<Message> messages = messageRepository.findLatestMessagesForUser(userId);
        return messages.stream()
                .map(MessageResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Xóa tin nhắn
     */
    public void deleteMessage(UUID messageId, UUID userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Tin nhắn không tồn tại"));
        
        // Chỉ cho phép người gửi xóa tin nhắn của mình
        if (!message.getSender().getId().equals(userId)) {
            throw new RuntimeException("Không có quyền xóa tin nhắn này");
        }
        
        messageRepository.delete(message);
    }
}

