package com.fooddelivery.controller;

import com.fooddelivery.dto.AdminUserResponse;
import com.fooddelivery.model.User;
import com.fooddelivery.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - User Management", description = "User management endpoints (requires ADMIN role)")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    @Operation(
            summary = "List all users",
            description = "Retrieves a paginated list of all users with optional filtering by role. Supports pagination."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<Page<AdminUserResponse>> getAllUsers(
            @Parameter(description = "Filter by user role (CUSTOMER, RESTAURANT, DELIVERY, ADMIN)", example = "CUSTOMER")
            @RequestParam(required = false) User.Role role,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Page<AdminUserResponse> users = adminService.getAllUsers(role, page, size);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/activate")
    @Operation(
            summary = "Activate or deactivate user",
            description = "Activates or deactivates a user account. Deactivated users cannot login or use the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User status updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<AdminUserResponse> activateUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Activate (true) or deactivate (false)", required = true, example = "true")
            @RequestParam Boolean isActive) {
        AdminUserResponse response = adminService.activateUser(id, isActive);
        return ResponseEntity.ok(response);
    }
}

