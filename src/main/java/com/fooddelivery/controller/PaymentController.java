package com.fooddelivery.controller;

import com.fooddelivery.dto.CreatePaymentRequest;
import com.fooddelivery.dto.PaymentResponse;
import com.fooddelivery.dto.RefundRequest;
import com.fooddelivery.dto.VerifyPaymentRequest;
import com.fooddelivery.service.PaymentService;
import com.fooddelivery.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Payment processing endpoints (requires CUSTOMER authentication). Rate limited to 10 requests/minute.")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping("/create-order")
    @Operation(summary = "Create payment order", description = "Creates a Razorpay payment order for an existing order. Returns payment details including Razorpay order_id.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Payment order created successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
    })
    public ResponseEntity<PaymentResponse> createPaymentOrder(
            Authentication authentication,
            @Valid @RequestBody CreatePaymentRequest request) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        PaymentResponse response = paymentService.createPaymentOrder(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(
            Authentication authentication,
            @Valid @RequestBody VerifyPaymentRequest request) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        PaymentResponse response = paymentService.verifyPayment(request, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refund")
    public ResponseEntity<PaymentResponse> processRefund(
            Authentication authentication,
            @Valid @RequestBody RefundRequest request) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        // Note: In production, only admins/restaurants should be able to refund
        PaymentResponse response = paymentService.processRefund(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(
            Authentication authentication,
            @PathVariable Long orderId) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        PaymentResponse response = paymentService.getPaymentByOrderId(orderId, userId);
        return ResponseEntity.ok(response);
    }
}


