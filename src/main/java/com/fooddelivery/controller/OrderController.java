package com.fooddelivery.controller;

import com.fooddelivery.dto.OrderResponse;
import com.fooddelivery.dto.PlaceOrderRequest;
import com.fooddelivery.model.Order;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management endpoints (requires CUSTOMER authentication)")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping("/place")
    @Operation(
            summary = "Place a new order",
            description = "Places an order from the current cart. Cart is cleared after successful order placement."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or empty cart"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<OrderResponse> placeOrder(
            Authentication authentication,
            @Valid @RequestBody PlaceOrderRequest request) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        OrderResponse response = orderService.placeOrder(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getCustomerOrders(
            Authentication authentication,
            @RequestParam(required = false) Order.OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orders = orderService.getCustomerOrders(userId, status, pageable);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        OrderResponse response = orderService.cancelOrder(userId, id);
        return ResponseEntity.ok(response);
    }
}

