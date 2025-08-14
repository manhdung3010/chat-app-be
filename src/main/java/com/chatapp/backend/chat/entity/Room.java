 package com.chatapp.backend.chat.entity;

import com.chatapp.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    @Builder.Default
    private RoomType roomType = RoomType.GROUP;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "room_members",
        joinColumns = @JoinColumn(name = "room_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<User> members = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "room_admins",
        joinColumns = @JoinColumn(name = "room_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<User> admins = new HashSet<>();
    
    @Column(name = "is_private", nullable = false)
    @Builder.Default
    private Boolean isPrivate = false;
    
    @Column(name = "max_members")
    private Integer maxMembers;
    
    @Column(name = "current_member_count", nullable = false)
    @Builder.Default
    private Integer currentMemberCount = 0;
    
    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Enum cho loại phòng
    public enum RoomType {
        PRIVATE,    // Chat 1-1 (tự động tạo)
        GROUP,      // Group chat
        CHANNEL     // Channel (broadcast)
    }
    
    // Helper methods
    public void addMember(User user) {
        if (members == null) {
            members = new HashSet<>();
        }
        members.add(user);
        currentMemberCount = members.size();
    }
    
    public void removeMember(User user) {
        if (members != null) {
            members.remove(user);
            currentMemberCount = members.size();
        }
    }
    
    public void addAdmin(User user) {
        if (admins == null) {
            admins = new HashSet<>();
        }
        admins.add(user);
    }
    
    public void removeAdmin(User user) {
        if (admins != null) {
            admins.remove(user);
        }
    }
    
    public boolean isMember(User user) {
        return members != null && members.contains(user);
    }
    
    public boolean isAdmin(User user) {
        return admins != null && admins.contains(user);
    }
    
    public boolean isCreator(User user) {
        return createdBy != null && createdBy.getId().equals(user.getId());
    }
}

