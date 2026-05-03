package com.saga.order.service;

import com.saga.order.dto.CreateOrderRequest;
import com.saga.order.entity.Order;
import com.saga.order.entity.OrderStatus;
import com.saga.order.event.OrderCreatedEvent;
import com.saga.order.event.OrderEventPublisher;
import com.saga.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        log.info("Creating order | customerId = {} | productId = {} | quantity = {}",
                request.getCustomerId(), request.getProductId(), request.getQuantity());

        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .totalAmount(request.getTotalAmount())
                .status(OrderStatus.PENDING)
                .build();

        Order saveOrder = orderRepository.save(order);
        log.info("Order saved to DB | orderId = {}", saveOrder.getId());

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .orderId(saveOrder.getId())
                .customerId(saveOrder.getCustomerId())
                .productId(saveOrder.getProductId())
                .quantity(saveOrder.getQuantity())
                .totalAmount(saveOrder.getTotalAmount())
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publishOrderCreated(event);

        return saveOrder;
    }

    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }
}
