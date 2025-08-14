package com.chatapp.backend.chat.dto;

import com.chatapp.backend.chat.entity.Room;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    
    @NotBlank(message = "Tên phòng không được để trống")
    @Size(min = 1, max = 100, message = "Tên phòng phải từ 1 đến 100 ký tự")
    private String name;
    
    @Size(max = 500, message = "Mô tả phòng không được quá 500 ký tự")
    private String description;
    
    private String avatarUrl;
    
    @NotNull(message = "Loại phòng không được để trống")
    private Room.RoomType roomType;
    
    private List<UUID> memberIds;
    
    @Builder.Default
    private Boolean isPrivate = false;
    
    private Integer maxMembers;
}

