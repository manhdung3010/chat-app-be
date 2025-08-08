package com.chatapp.backend.admin.controller;

import com.chatapp.backend.admin.dto.CreateUserRequest;
import com.chatapp.backend.admin.dto.UpdateUserRequest;
import com.chatapp.backend.admin.dto.UserDto;
import com.chatapp.backend.admin.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Admin management APIs")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminUserService adminUserService;
    
    @GetMapping("/dashboard")
    @Operation(summary = "Admin dashboard", description = "Admin dashboard endpoint - only accessible by admin users")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Welcome to Admin Dashboard!");
    }
    
    @GetMapping("/stats")
    @Operation(summary = "System statistics", description = "Get system statistics - admin only")
    public ResponseEntity<String> getSystemStats() {
        return ResponseEntity.ok("System statistics (admin only)");
    }
    
    // User Management APIs
    
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Get list of all users - admin only")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID", description = "Get user details by ID - admin only")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        UserDto user = adminUserService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/users")
    @Operation(summary = "Create new user", description = "Create a new user - admin only")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDto createdUser = adminUserService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @PutMapping("/users/{id}")
    @Operation(summary = "Update user", description = "Update existing user - admin only")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        UserDto updatedUser = adminUserService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete user", description = "Delete user by ID - admin only")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/users/{id}/promote")
    @Operation(summary = "Promote user to admin", description = "Promote user to admin role - admin only")
    public ResponseEntity<UserDto> promoteToAdmin(@PathVariable UUID id) {
        UserDto promotedUser = adminUserService.promoteToAdmin(id);
        return ResponseEntity.ok(promotedUser);
    }
    
    @PatchMapping("/users/{id}/demote")
    @Operation(summary = "Demote admin to user", description = "Demote admin to user role - admin only")
    public ResponseEntity<UserDto> demoteToUser(@PathVariable UUID id) {
        UserDto demotedUser = adminUserService.demoteToUser(id);
        return ResponseEntity.ok(demotedUser);
    }
}
