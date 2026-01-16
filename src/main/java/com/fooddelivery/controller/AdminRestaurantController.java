package com.fooddelivery.controller;

import com.fooddelivery.dto.RestaurantResponse;
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
@RequestMapping("/api/admin/restaurants")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Restaurant Management", description = "Restaurant approval and management endpoints (requires ADMIN role)")
@SecurityRequirement(name = "bearerAuth")
public class AdminRestaurantController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/pending-approval")
    @Operation(
            summary = "Get restaurants awaiting approval",
            description = "Retrieves a paginated list of restaurants that are pending approval by admin."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pending restaurants retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<Page<RestaurantResponse>> getPendingRestaurants(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Page<RestaurantResponse> restaurants = adminService.getPendingRestaurants(page, size);
        return ResponseEntity.ok(restaurants);
    }

    @PutMapping("/{id}/approve")
    @Operation(
            summary = "Approve or reject restaurant",
            description = "Approves or rejects a restaurant. Only approved restaurants are visible to customers and can receive orders."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant approval status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<RestaurantResponse> approveRestaurant(
            @Parameter(description = "Restaurant ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Approve (true) or reject (false)", required = true, example = "true")
            @RequestParam Boolean isApproved) {
        RestaurantResponse response = adminService.approveRestaurant(id, isApproved);
        return ResponseEntity.ok(response);
    }
}

