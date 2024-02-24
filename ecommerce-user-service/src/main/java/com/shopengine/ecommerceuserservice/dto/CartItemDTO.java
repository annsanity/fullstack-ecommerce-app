package com.shopengine.ecommerceuserservice.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private Long productId;
    private int quantity;
}
