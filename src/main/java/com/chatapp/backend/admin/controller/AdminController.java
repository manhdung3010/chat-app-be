package com.chatapp.backend.admin.controller;

import com.chatapp.backend.common.annotations.ApiResponseGroups;
import com.chatapp.backend.common.constants.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Dashboard", description = "Admin dashboard and system management APIs")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @GetMapping("/dashboard")
    @Operation(
        summary = "Admin dashboard",
        description = "Admin dashboard endpoint - only accessible by admin users"
    )
    @ApiResponseGroups.AdminAuthResponses
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Successfully retrieved dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Welcome to Admin Dashboard!");
    }
    
    @GetMapping("/stats")
    @Operation(
        summary = "System statistics",
        description = "Get system statistics - admin only"
    )
    @ApiResponseGroups.AdminAuthResponses
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Successfully retrieved statistics")
    public ResponseEntity<String> getSystemStats() {
        return ResponseEntity.ok("System statistics (admin only)");
    }
}
