package com.fooddelivery.dto;

import com.fooddelivery.model.CustomerAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    
    @NotBlank(message = "Street is required")
    private String street;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "Pincode is required")
    private String pincode;
    
    @NotNull(message = "Latitude is required")
    private Double lat;
    
    @NotNull(message = "Longitude is required")
    private Double longitude;
    
    @NotNull(message = "Address type is required")
    private CustomerAddress.AddressType addressType;
    
    private Boolean isDefault = false;
}

