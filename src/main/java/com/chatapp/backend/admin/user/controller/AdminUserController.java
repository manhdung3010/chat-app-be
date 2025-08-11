package com.chatapp.backend.admin.user.controller;

import com.chatapp.backend.admin.user.dto.CreateUserRequest;
import com.chatapp.backend.admin.user.dto.UpdateUserRequest;
import com.chatapp.backend.admin.user.dto.UserDto;
import com.chatapp.backend.admin.user.service.AdminUserService;
import com.chatapp.backend.common.annotations.ApiResponseGroups;
import com.chatapp.backend.common.constants.AppConstants;
import com.chatapp.backend.common.constants.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(AppConstants.ADMIN_USER_PATH)
@Tag(name = "Admin User Management", description = "Admin APIs for user management operations")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {
    
    private final AdminUserService adminUserService;
    
    @GetMapping
    @Operation(
        summary = "Get all users",
        description = "Retrieve list of all users in the system - Admin only"
    )
    @ApiResponseGroups.AdminAuthResponses
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Successfully retrieved users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieve user details by user ID - Admin only"
    )
    @ApiResponseGroups.AdminCrudResponses
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Successfully retrieved user")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        UserDto user = adminUserService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    @Operation(
        summary = "Create new user",
        description = "Create a new user account - Admin only"
    )
    @ApiResponseGroups.AdminCreateResponses
    @ApiResponse(responseCode = HttpStatusCodes.CREATED, description = "User created successfully")
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        UserDto createdUser = adminUserService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update user",
        description = "Update existing user information - Admin only"
    )
    @ApiResponseGroups.AdminUpdateResponses
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "User updated successfully")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserDto updatedUser = adminUserService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete user",
        description = "Delete user by ID - Admin only"
    )
    @ApiResponseGroups.AdminCrudResponses
    @ApiResponse(responseCode = HttpStatusCodes.NO_CONTENT, description = "User deleted successfully")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/promote")
    @Operation(
        summary = "Promote user to admin",
        description = "Promote user to admin role - Admin only"
    )
    @ApiResponseGroups.AdminCrudResponses
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "User promoted successfully")
    public ResponseEntity<UserDto> promoteToAdmin(
            @Parameter(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        UserDto promotedUser = adminUserService.promoteToAdmin(id);
        return ResponseEntity.ok(promotedUser);
    }
    
    @PatchMapping("/{id}/demote")
    @Operation(
        summary = "Demote admin to user",
        description = "Demote admin to user role - Admin only"
    )
    @ApiResponseGroups.AdminCrudResponses
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "User demoted successfully")
    public ResponseEntity<UserDto> demoteToUser(
            @Parameter(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        UserDto demotedUser = adminUserService.demoteToUser(id);
        return ResponseEntity.ok(demotedUser);
    }
}
