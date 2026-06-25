package com.quickbite.dto;

import lombok.Data;

@Data
public class PlaceOrderRequest {
    private Long restaurantId;
    private String deliveryAddress;
}
