package com.fooddelivery.dto;

import com.fooddelivery.model.CustomerAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private String street;
    private String city;
    private String pincode;
    private Double lat;
    private Double longitude;
    private CustomerAddress.AddressType addressType;
    private Boolean isDefault;
}

