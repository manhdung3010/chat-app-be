package com.chatapp.backend.user.controller;

import com.chatapp.backend.common.annotations.CurrentUserId;
import com.chatapp.backend.common.constants.AppConstants;
import com.chatapp.backend.common.constants.HttpStatusCodes;
import com.chatapp.backend.common.constants.MessageConstants;
import com.chatapp.backend.common.dto.ApiResponse;
import com.chatapp.backend.common.utils.ResponseUtils;
import com.chatapp.backend.user.dto.UserListItemDto;
import com.chatapp.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(AppConstants.USER_PATH)
@RequiredArgsConstructor
@Tag(name = "Users", description = "APIs for listing users to chat")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Danh sách người dùng để nhắn tin", description = "Trả về danh sách người dùng (loại trừ bản thân), hỗ trợ tìm kiếm theo username/email và phân trang")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = HttpStatusCodes.OK, description = "Lấy danh sách thành công")
    public ResponseEntity<ApiResponse<java.util.List<UserListItemDto>>> listUsers(
            @CurrentUserId UUID currentUserId,
            @Parameter(description = "Từ khóa tìm kiếm theo username hoặc email") @RequestParam(value = "q", required = false) String q,
            @Parameter(description = "Trang hiện tại (bắt đầu từ 1)", example = "1") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "Số lượng mỗi trang", example = "20") @RequestParam(value = "limit", defaultValue = "20") int limit) {

        int zeroBasedPage = Math.max(0, page - 1);
        int pageSize = Math.max(1, Math.min(limit, 100));
        Pageable pageable = PageRequest.of(zeroBasedPage, pageSize);

        Page<UserListItemDto> result = userService.listUsersForChat(currentUserId, q, pageable);
        return ResponseEntity.ok(ResponseUtils.createPaginatedResponse(result, page, pageSize, MessageConstants.USERS_RETRIEVED));
    }
}


