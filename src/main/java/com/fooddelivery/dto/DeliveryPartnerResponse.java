package com.fooddelivery.dto;

import com.fooddelivery.model.DeliveryPartner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerResponse {
    private Long id;
    private Long userId;
    private String name;
    private DeliveryPartner.VehicleType vehicleType;
    private Boolean isAvailable;
    private Double currentLat;
    private Double currentLong;
    private String email;
    private String phone;
}

