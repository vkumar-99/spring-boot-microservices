package com.javalang.bokstore.order.domain;

import com.javalang.bokstore.order.domain.models.OrderRequest;
import com.javalang.bokstore.order.domain.models.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    public OrderResponse createOrder(String userName, OrderRequest orderRequest) {
        orderValidator.validateOrder(orderRequest);
        OrderEntity orderEntity = OrderMapper.convertToEntity(orderRequest);
        orderEntity.setUserName(userName);
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        LOGGER.info("Created order with orderId: {}", savedOrder.getOrderNumber());
        return new OrderResponse(savedOrder.getOrderNumber());
    }
}
