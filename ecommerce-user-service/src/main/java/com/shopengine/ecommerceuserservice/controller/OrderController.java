package com.shopengine.ecommerceuserservice.controller;

import com.shopengine.ecommerceuserservice.dto.OrderDTO;
import com.shopengine.ecommerceuserservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Endpoint to place an order for the user's cart
    @PostMapping("/{userId}")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long userId) {
        OrderDTO orderDTO = orderService.createOrder(userId);
        return ResponseEntity.ok(orderDTO);
    }

    // Endpoint to get all orders for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}
