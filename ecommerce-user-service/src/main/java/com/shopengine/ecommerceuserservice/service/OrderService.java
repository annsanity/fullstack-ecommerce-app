package com.shopengine.ecommerceuserservice.service;

import com.shopengine.ecommerceuserservice.dto.CartDTO;
import com.shopengine.ecommerceuserservice.dto.CartItemDTO;
import com.shopengine.ecommerceuserservice.dto.OrderDTO;
import com.shopengine.ecommerceuserservice.dto.OrderItemDTO;
import com.shopengine.ecommerceuserservice.model.*;
import com.shopengine.ecommerceuserservice.repository.OrderRepository;
import com.shopengine.ecommerceuserservice.repository.ProductRepository;
import com.shopengine.ecommerceuserservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    private final UserRepository userRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CartService cartService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    // Method to create an order from cart items
    public OrderDTO createOrder(Long userId) {
        // Retrieve CartDTO from CartService
        CartDTO cartDTO = cartService.getCartByUserId(userId);

        if (cartDTO == null || cartDTO.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new Order and set its properties based on CartDTO
        Order order = new Order();
        order.setUser(user);  // Assuming User is fetched or initialized elsewhere

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        // Loop through items in CartDTO and create OrderItems
        for (CartItemDTO cartItemDTO : cartDTO.getItems()) {
            Product product = productRepository.findById(cartItemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItemDTO.getQuantity());
            orderItem.setPrice(product.getPrice()); // Assuming product price is used
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            totalAmount += orderItem.getPrice() * orderItem.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        return mapToOrderDTO(order);
    }

    // Method to retrieve all orders for a user
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(mapToOrderDTO(order));
        }
        return orderDTOs;
    }

    // Helper method to map Order to OrderDTO
    private OrderDTO mapToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUser().getId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalAmount(order.getTotalAmount());

        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());
            itemDTOs.add(itemDTO);
        }

        orderDTO.setItems(itemDTOs);
        return orderDTO;
    }
}
