package com.fooddelivery.controller;

import com.fooddelivery.dto.DeliveryPartnerResponse;
import com.fooddelivery.dto.EarningsResponse;
import com.fooddelivery.dto.UpdateLocationRequest;
import com.fooddelivery.service.DeliveryPartnerService;
import com.fooddelivery.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryPartnerController {

    @Autowired
    private DeliveryPartnerService deliveryPartnerService;

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping("/profile")
    public ResponseEntity<DeliveryPartnerResponse> getProfile(Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        DeliveryPartnerResponse response = deliveryPartnerService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/toggle-availability")
    public ResponseEntity<DeliveryPartnerResponse> toggleAvailability(Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        DeliveryPartnerResponse response = deliveryPartnerService.toggleAvailability(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/earnings")
    public ResponseEntity<EarningsResponse> getEarnings(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        EarningsResponse response = deliveryPartnerService.getEarnings(userId, startDate, endDate);
        return ResponseEntity.ok(response);
    }
}

