package com.fooddelivery.controller;

import com.fooddelivery.dto.AddressRequest;
import com.fooddelivery.dto.AddressResponse;
import com.fooddelivery.dto.CustomerResponse;
import com.fooddelivery.dto.CustomerUpdateRequest;
import com.fooddelivery.service.CustomerService;
import com.fooddelivery.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping("/profile")
    public ResponseEntity<CustomerResponse> getCustomerProfile(Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        CustomerResponse response = customerService.getCustomerProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<CustomerResponse> updateCustomerProfile(
            Authentication authentication,
            @Valid @RequestBody CustomerUpdateRequest request) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        CustomerResponse response = customerService.updateCustomerProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressResponse> addAddress(
            Authentication authentication,
            @Valid @RequestBody AddressRequest request) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        AddressResponse response = customerService.addAddress(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressResponse>> getCustomerAddresses(Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        List<AddressResponse> addresses = customerService.getCustomerAddresses(userId);
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/addresses/{id}/set-default")
    public ResponseEntity<AddressResponse> setDefaultAddress(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        AddressResponse response = customerService.setDefaultAddress(userId, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        customerService.deleteAddress(userId, id);
        return ResponseEntity.noContent().build();
    }
}

