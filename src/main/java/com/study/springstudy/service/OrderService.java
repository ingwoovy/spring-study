package com.study.springstudy.service;

import com.study.springstudy.domain.Order;
import com.study.springstudy.domain.User;
import com.study.springstudy.dto.OrderCreateRequest;
import com.study.springstudy.dto.OrderResponse;
import com.study.springstudy.exception.OrderNotFoundException;
import com.study.springstudy.exception.UserNotFoundException;
import com.study.springstudy.repository.OrderRepository;
import com.study.springstudy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderCreateRequest request) {
        Long userId = request.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Order order = new Order(
                null,
                request.getProductName(),
                request.getPrice(),
                user
        );
        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getProductName(),
                savedOrder.getPrice(),
                savedOrder.getUser().getId(),
                savedOrder.getUser().getName()
        );
        }
        public OrderResponse getOrder (Long id) {
            Order order = orderRepository.findById(id)
                    .orElseThrow(OrderNotFoundException::new);

            return new OrderResponse(
                    order.getId(),
                    order.getProductName(),
                    order.getPrice(),
                    order.getUser().getId(),
                    order.getUser().getName()
            );
        }
        public List<OrderResponse> getAllOrder() {

            List<Order> orders = orderRepository.findAllWithUser();

            return orders.stream()
                    .map(order -> new OrderResponse(
                            order.getId(),
                            order.getProductName(),
                            order.getPrice(),
                            order.getUser().getId(),
                            order.getUser().getName()
                    ))
                    .toList();
        }
}
