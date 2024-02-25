package com.shopengine.ecommerceuserservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long orderId;      // Only the ID of the Order
    private Long productId;    // Only the ID of the Product
    private int quantity;
    private double price;
}
