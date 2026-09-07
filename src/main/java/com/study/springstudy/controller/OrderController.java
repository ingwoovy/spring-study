package com.study.springstudy.controller;

import com.study.springstudy.dto.OrderCreateRequest;
import com.study.springstudy.dto.OrderResponse;
import com.study.springstudy.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/{id}")
    public OrderResponse getOrder (@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/orders")
    public List<OrderResponse> getAllOrder () {
        return orderService.getAllOrder();
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder (
            @RequestBody OrderCreateRequest request
            ) {
        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
