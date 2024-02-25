package com.shopengine.ecommerceuserservice.dto;

import com.shopengine.ecommerceuserservice.model.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private List<OrderItemDTO> items = new ArrayList<>();
}
