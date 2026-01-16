package com.fooddelivery.controller;

import com.fooddelivery.dto.OrderResponse;
import com.fooddelivery.model.Order;
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
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Order Management", description = "Order management and viewing endpoints (requires ADMIN role)")
@SecurityRequirement(name = "bearerAuth")
public class AdminOrderController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/all")
    @Operation(
            summary = "Get all orders",
            description = "Retrieves a paginated list of all orders in the system with optional filtering by status. Admin can view orders from all customers."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @Parameter(description = "Filter by order status", example = "DELIVERED")
            @RequestParam(required = false) Order.OrderStatus status,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Page<OrderResponse> orders = adminService.getAllOrders(status, page, size);
        return ResponseEntity.ok(orders);
    }
}

