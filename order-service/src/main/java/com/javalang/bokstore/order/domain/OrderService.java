package com.javalang.bokstore.order.domain;

import com.javalang.bokstore.order.domain.models.OrderRequest;
import com.javalang.bokstore.order.domain.models.OrderResponse;
import com.javalang.bokstore.order.domain.models.OrderStatus;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> ALLOWED_COUNTRIES = List.of("INDIA", "USA", "GERMANY", "UK");
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    public OrderService(
            OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderEventService = orderEventService;
    }

    public OrderResponse createOrder(String userName, OrderRequest orderRequest) {
        orderValidator.validateOrder(orderRequest);
        OrderEntity orderEntity = OrderMapper.convertToEntity(orderRequest);
        orderEntity.setUserName(userName);
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        LOGGER.info("Created order with orderId: {}", savedOrder.getOrderNumber());
        orderEventService.save(OrderEventMapper.buildOrderCreatedEvent(savedOrder));
        return new OrderResponse(savedOrder.getOrderNumber());
    }

    public void processNewOrders() {
        List<OrderEntity> newOrderEntities = orderRepository.findByStatus(OrderStatus.NEW);
        newOrderEntities.stream().forEach(this::process);
    }

    private void process(OrderEntity order) {
        try {
            if (canBeDelivered(order)) {
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
            } else {
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(OrderEventMapper.buildOrderCancelledEvent(order, "Location not serviceable"));
            }
        } catch (RuntimeException e) {
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, e.getMessage()));
        }
    }

    private boolean canBeDelivered(OrderEntity order) {
        return ALLOWED_COUNTRIES.contains(order.getDeliveryAddress().country().toUpperCase());
    }
}
